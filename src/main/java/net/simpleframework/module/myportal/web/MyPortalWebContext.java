package net.simpleframework.module.myportal.web;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.Module;
import net.simpleframework.module.myportal.impl.MyPortalContext;
import net.simpleframework.module.myportal.web.page.MyPortalPage;
import net.simpleframework.mvc.ctx.WebModuleFunction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyPortalWebContext extends MyPortalContext {

	@Override
	protected Module createModule() {
		return super.createModule().setDefaultFunction(
				new WebModuleFunction(MyPortalPage.class)
						.setName("simple-module-myportal-MyPortalPage").setText($m("MyPortalContext.0"))
						.setDisabled(true));
	}
}
