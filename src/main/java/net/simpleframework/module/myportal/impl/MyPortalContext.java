package net.simpleframework.module.myportal.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.common.DbTable;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.ado.AbstractADOModuleContext;
import net.simpleframework.module.myportal.ILayoutLobManager;
import net.simpleframework.module.myportal.IMyPortalContext;
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
public abstract class MyPortalContext extends AbstractADOModuleContext implements IMyPortalContext {

	@Override
	public void onInit() throws Exception {
		super.onInit();
		dataServiceFactory.putEntityService(PortalTabBean.class, new DbTable("sf_my_portal_tabs"))
				.putEntityService(LayoutLobBean.class, new DbTable("sf_my_portal_layout", true));
	}

	@Override
	protected Module createModule() {
		return new Module().setName("simple-module-myportal").setText($m("MyPortalContext.0"))
				.setOrder(3);
	}

	@Override
	public IPortalTabManager getPortalTabManager() {
		return singleton(PortalTabManager.class);
	}

	@Override
	public ILayoutLobManager getMyPortalManager() {
		return singleton(LayoutLobManager.class);
	}
}
