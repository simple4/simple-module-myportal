package net.simpleframework.module.myportal;

import java.util.Collection;

import net.simpleframework.common.ID;
import net.simpleframework.ctx.ado.IBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPortalTabManager extends IBeanManagerAware<PortalTabBean> {

	/**
	 * 获取指定用户缺省的门户页面
	 * 
	 * @param rRequest
	 * @param accountId
	 * @return
	 */
	PortalTabBean homeTab(ID accountId);

	/**
	 * 获取我的门户标签列表
	 * 
	 * @param rRequest
	 * @return
	 */
	Collection<PortalTabBean> queryTabs(ID accountId);
}
