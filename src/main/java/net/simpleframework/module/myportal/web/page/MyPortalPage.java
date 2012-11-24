package net.simpleframework.module.myportal.web.page;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.module.myportal.IMyPortalContext;
import net.simpleframework.module.myportal.IPortalTabManager;
import net.simpleframework.module.myportal.MyPortalContextFactory;
import net.simpleframework.module.myportal.PortalTabBean;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.portal.PortalBean;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.tooltip.TooltipBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.TabButton;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.FixedTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyPortalPage extends FixedTemplatePage {
	public static IMyPortalContext context = MyPortalContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addImportCSS(new String[] { getCssResourceHomePath(pParameter) + "/my_portal.css" });

		addComponentBean(pParameter, PortalBean.class, MyPortalHandle.class).setContainerId(
				"MyPortalPage_layout");

		addAjaxRequest(pParameter, "MyPortalPage_tabPage", PortalTabEditPage.class);
		addComponentBean(pParameter, "MyPortalPage_addTab", WindowBean.class)
				.setContentRef("MyPortalPage_tabPage").setTitle($m("MyPortalPage.0")).setHeight(250)
				.setWidth(350);

		addAjaxRequest(pParameter, "MyPortalPage_tabDelete").setConfirmMessage($m("MyPortalPage.1"))
				.setHandleMethod("doTabDelete");

		addComponentBean(pParameter, "MyPortalPage_tooltip", TooltipBean.class);

		final MenuBean menu = (MenuBean) addComponentBean(pParameter, "MyPortalPage_menu",
				MenuBean.class).setMenuEvent(EMenuEvent.click).setSelector(".MyPortalPage .tmenu");
		menu.addItem(new MenuItem(menu)
				.setTitle($m("Edit"))
				.setIconClass(MenuItem.ICON_EDIT)
				.setJsSelectCallback(
						"$Actions['MyPortalPage_addTab']('tab_id=' + $Target(item).id.substring(1));"));
		menu.addItem(MenuItem.sep(menu));
		menu.addItem(new MenuItem(menu)
				.setTitle($m("Delete"))
				.setIconClass(MenuItem.ICON_DELETE)
				.setJsSelectCallback(
						"$Actions['MyPortalPage_tabDelete']('tab_id=' + $Target(item).id.substring(1));"));
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	public IForward doTabDelete(final ComponentParameter cParameter) {
		final IPortalTabManager tabMgr = context.getPortalTabManager();
		final PortalTabBean homeTab = tabMgr.getBean(cParameter.getParameter("tab_id"));
		final PortalTabBean firstHomeTab = tabMgr.homeTab(permission().getLoginId(cParameter));
		final JavascriptForward js = new JavascriptForward();
		if (firstHomeTab.getId().equals(homeTab.getId())) {
			js.append("alert('").append($m("MyPortalPage.3")).append("');");
		} else {
			tabMgr.delete(homeTab.getId());
			js.append("$Actions.loc(\"");
			js.append(MyPortalHandle.getTabUrl(homeTab.getId())).append("\");");
		}
		return js;
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(
				new LinkElement($m("MyPortalContext.0")).setHref(uriFor(MyPortalPage.class)));
	}

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String variable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='MyPortalPage'>");
		sb.append("  <div class='bar'>");
		sb.append("    <a onclick=\"_lo_fireMenuAction(")
				.append("$('MyPortalPage_layout').down('.pagelet'), 'layoutModulesWindow');\">")
				.append($m("MyPortalPage.2")).append("</a>");
		sb.append("  </div>");
		sb.append("  <div class='tabs_icon'></div>");
		sb.append("  <div class='tabs'>");
		final TabButtons btns = TabButtons.of();
		for (final PortalTabBean homeTab : context.getPortalTabManager().queryTabs(
				permission().getLoginId(pParameter))) {
			btns.add(new TabButton(homeTab.getTabText(), MyPortalHandle.getTabUrl(homeTab.getId()))
					.setId(Convert.toString(homeTab.getId())).setMenuIcon(true)
					.setTitle(homeTab.getDescription()));
		}
		sb.append(btns.toString(pParameter));
		sb.append("    <a class='addtab' onclick=\"$Actions['MyPortalPage_addTab']();\">")
				.append($m("MyPortalPage.0")).append("</a>");
		sb.append("  </div>");
		sb.append("</div>");
		sb.append("<div id='MyPortalPage_layout'></div>");
		return sb.toString();
	}
}
