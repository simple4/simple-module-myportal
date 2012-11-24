package net.simpleframework.module.myportal.impl;

import static net.simpleframework.common.I18n.$m;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.ado.IParamsValue;
import net.simpleframework.module.myportal.ETabMark;
import net.simpleframework.module.myportal.IPortalTabManager;
import net.simpleframework.module.myportal.LayoutLobBean;
import net.simpleframework.module.myportal.PortalTabBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalTabManager extends AbstractPortalManager<PortalTabBean> implements
		IPortalTabManager {

	private PortalTabBean createHomeTab(final ID accountId) {
		final PortalTabBean tab = createBean();
		tab.setUserId(accountId);
		tab.setCreateDate(new Date());
		tab.setTabMark(ETabMark.home);
		tab.setTabText($m("MyPortalContext.0"));
		return tab;
	}

	@Override
	public PortalTabBean homeTab(final ID accountId) {
		if (accountId == null) {
			return null;
		}
		PortalTabBean tab = getBean("userid=? and tabmark=?", accountId, ETabMark.home);
		if (tab == null) {
			insert(tab = createHomeTab(accountId));
		}
		return tab;
	}

	@Override
	public Collection<PortalTabBean> queryTabs(final ID accountId) {
		final ArrayList<PortalTabBean> al = new ArrayList<PortalTabBean>();
		if (accountId != null) {
			final IQueryEntitySet<PortalTabBean> qs = query("userId=? order by tabmark desc",
					accountId);
			boolean homeTab = false;
			PortalTabBean tab;
			while ((tab = qs.next()) != null) {
				al.add(tab);
				if (tab.getTabMark() == ETabMark.home) {
					homeTab = true;
				}
			}
			if (!homeTab) {
				insert(tab = createHomeTab(accountId));
				al.add(0, tab);
			}
		}
		return al;
	}

	{
		addListener(new TableEntityAdapterEx() {
			@Override
			public void afterDelete(final ITableEntityService service, final IParamsValue paramsValue) {
				super.afterDelete(service, paramsValue);
				for (final PortalTabBean tab : coll(paramsValue)) {
					myPortalLayoutMgr().delete(tab.getId());
				}
			}

			@Override
			public void afterInsert(final ITableEntityService service, final Object[] beans) {
				super.afterInsert(service, beans);
				for (final Object bean : beans) {
					final PortalTabBean tab = (PortalTabBean) bean;
					final LayoutLobBean lob = new LayoutLobBean();
					lob.setId(tab.getId());
					try {
						final InputStream is = ClassUtils.getResourceRecursively(PortalTabManager.class,
								"template_" + tab.getTabMark().name() + ".xml");
						lob.setLayoutLob(new InputStreamReader(is, "utf-8"));
					} catch (final UnsupportedEncodingException e) {
					}
					myPortalLayoutMgr().insert(lob);
				}
			}
		});
	}
}
