package net.simpleframework.module.myportal;

import net.simpleframework.ctx.ado.IADOModuleContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IMyPortalContext extends IADOModuleContext {

	/**
	 * 获取我的门户标签页管理器
	 * 
	 * @return
	 */
	IPortalTabManager getPortalTabManager();

	/**
	 * 获取门户布局管理器
	 * 
	 * @return
	 */
	ILayoutLobManager getMyPortalManager();
}
