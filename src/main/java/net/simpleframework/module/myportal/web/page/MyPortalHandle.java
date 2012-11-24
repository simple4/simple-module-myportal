package net.simpleframework.module.myportal.web.page;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.ID;
import net.simpleframework.common.IoUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.xml.XmlDocument;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.module.myportal.ILayoutLobManager;
import net.simpleframework.module.myportal.IMyPortalContext;
import net.simpleframework.module.myportal.IPortalTabManager;
import net.simpleframework.module.myportal.LayoutLobBean;
import net.simpleframework.module.myportal.MyPortalContextFactory;
import net.simpleframework.module.myportal.PortalTabBean;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.LocalSessionCache;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.portal.ColumnBean;
import net.simpleframework.mvc.component.portal.DefaultPortalHandler;
import net.simpleframework.mvc.component.portal.PortalBean;
import net.simpleframework.mvc.component.portal.PortalRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyPortalHandle extends DefaultPortalHandler {

	public static final IMyPortalContext context = MyPortalContextFactory.get();

	@Override
	public Collection<ColumnBean> getPortalColumns(final ComponentParameter cParameter) {
		final ColumnCache columnCache = getColumnCache(cParameter, getHomeTab(cParameter));
		return columnCache != null ? columnCache.columns : null;
	}

	protected ColumnCache getColumnCache(final ComponentParameter cParameter,
			final PortalTabBean homeTab) {
		if (homeTab == null) {
			return null;
		}
		final ID tabId = homeTab.getId();
		final ColumnCache columnCache = (ColumnCache) LocalSessionCache.get(cParameter.getSession(),
				tabId);
		if (columnCache != null) {
			return columnCache;
		}

		final LayoutLobBean homeLayout = context.getMyPortalManager().getBean(tabId);
		if (homeLayout == null) {
			return null;
		}
		final ColumnCache[] columnCacheArr = new ColumnCache[1];
		try {
			new XmlDocument(IoUtils.getStringFromReader(homeLayout.getLayoutLob())) {
				@Override
				protected void init() throws Exception {
					columnCacheArr[0] = new ColumnCache(this,
							((PortalRegistry) AbstractComponentRegistry
									.getComponentRegistry(PortalBean.class)).loadBean(cParameter,
									(PortalBean) cParameter.componentBean, getRoot()));
					LocalSessionCache.put(cParameter.getSession(), tabId, columnCacheArr[0]);
				}
			};
		} catch (final IOException e) {
			log.warn(e);
		}
		return columnCacheArr[0];
	}

	@Override
	public void updatePortal(final ComponentParameter cParameter,
			final Collection<ColumnBean> columns, final boolean draggable) {
		final PortalTabBean homeTab = getHomeTab(cParameter);
		if (homeTab == null) {
			return;
		}
		final ID tabId = homeTab.getId();

		final ILayoutLobManager portalLobMgr = context.getMyPortalManager();
		final LayoutLobBean homeLayout = portalLobMgr.getBean(tabId);
		final ColumnCache columnCache = (ColumnCache) LocalSessionCache.get(cParameter.getSession(),
				tabId);
		if (columnCache == null) {
			return;
		}
		final XmlElement root = columnCache.document.getRoot();
		root.clearContent();
		root.addAttribute("draggable", String.valueOf(draggable));
		columnCache.columns = columns;
		for (final ColumnBean column : columns) {
			column.syncElement();
			root.add(column.getBeanElement());
		}
		homeLayout.setLayoutLob(new StringReader(columnCache.document.toString()));
		portalLobMgr.update(homeLayout);
	}

	private PortalTabBean getHomeTab(final ComponentParameter cParameter) {
		final String tabId = cParameter.getParameter(TAB_ID);
		final IPortalTabManager portalMgr = context.getPortalTabManager();
		PortalTabBean homeTab = portalMgr.getBean(tabId);
		if (homeTab == null) {
			homeTab = portalMgr.homeTab(permission().getLoginId(cParameter));
		}
		return homeTab;
	}

	private class ColumnCache {
		XmlDocument document;

		Collection<ColumnBean> columns;

		ColumnCache(final XmlDocument document, final Collection<ColumnBean> columns) {
			this.document = document;
			this.columns = columns;
		}
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		if ("roleManager".equals(beanProperty)) {
			return "sys_account_normal";
		} else if ("draggable".equals(beanProperty)) {
			final ColumnCache columnCache = getColumnCache(cParameter, getHomeTab(cParameter));
			if (columnCache == null) {
				return false;
			}
			return Convert.toBool(columnCache.document.getRoot().attributeValue("draggable"));
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add(TAB_ID,
				cParameter.getParameter(TAB_ID));
	}

	static final String TAB_ID = "tabid";

	public static String getTabUrl(final Object tabId) {
		final StringBuilder sb = new StringBuilder();
		sb.append(AbstractMVCPage.uriFor(MyPortalPage.class)).append("?").append(TAB_ID).append("=")
				.append(tabId);
		return sb.toString();
	}
}
