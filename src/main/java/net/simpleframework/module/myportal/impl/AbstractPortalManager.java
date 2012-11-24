package net.simpleframework.module.myportal.impl;

import java.io.Serializable;

import net.simpleframework.ctx.ado.AbstractBeanDbManager;
import net.simpleframework.module.myportal.IMyPortalContext;
import net.simpleframework.module.myportal.MyPortalContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPortalManager<T extends Serializable> extends
		AbstractBeanDbManager<T, T> {
	@Override
	public IMyPortalContext getModuleContext() {
		return MyPortalContextFactory.get();
	}

	protected PortalTabManager myPortalTabMgr() {
		return (PortalTabManager) getModuleContext().getPortalTabManager();
	}

	protected LayoutLobManager myPortalLayoutMgr() {
		return (LayoutLobManager) getModuleContext().getMyPortalManager();
	}
}
