package net.simpleframework.module.myportal.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Map;

import net.simpleframework.common.html.element.EInputType;
import net.simpleframework.common.html.element.InputElement;
import net.simpleframework.module.myportal.IMyPortalContext;
import net.simpleframework.module.myportal.IPortalTabManager;
import net.simpleframework.module.myportal.MyPortalContextFactory;
import net.simpleframework.module.myportal.PortalTabBean;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.template.FormTableRowTemplatePage;
import net.simpleframework.mvc.template.struct.RowField;
import net.simpleframework.mvc.template.struct.TableRow;
import net.simpleframework.mvc.template.struct.TableRows;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalTabEditPage extends FormTableRowTemplatePage {
	public static IMyPortalContext context = MyPortalContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addFormValidationBean(pParameter).addValidators(
				new Validator().setSelector("#tab_name").setMethod(EValidatorMethod.required));
	}

	@Override
	public void onLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final PortalTabBean homeTab = context.getPortalTabManager().getBean(
				pParameter.getParameter("tab_id"));
		if (homeTab != null) {
			dataBinding.put("tab_id", homeTab.getId());
			dataBinding.put("tab_name", homeTab.getTabText());
			dataBinding.put("tab_description", homeTab.getDescription());
		}
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		final String tabText = cParameter.getParameter("tab_name");
		final String description = cParameter.getParameter("tab_description");
		final IPortalTabManager tabMgr = context.getPortalTabManager();
		PortalTabBean tab = tabMgr.getBean(cParameter.getParameter("tab_id"));
		if (tab == null) {
			tab = tabMgr.createBean();
			tab.setUserId(permission().getLoginId(cParameter));
			tab.setCreateDate(new Date());
			tab.setTabText(tabText);
			tab.setDescription(description);
			tabMgr.insert(tab);
		} else {
			tab.setTabText(tabText);
			tab.setDescription(description);
			tabMgr.update(tab);
		}
		final JavascriptForward js = super.doSave(cParameter);
		js.append("$Actions.loc(\"").append(MyPortalHandle.getTabUrl(tab.getId())).append("\");");
		return js;
	}

	private final InputElement tab_id = new InputElement("tab_id", EInputType.hidden);
	private final InputElement tab_name = new InputElement("tab_name");
	private final InputElement tab_description = new InputElement("tab_description",
			EInputType.textarea).setRows(6);

	@Override
	protected TableRows tableRows(final PageParameter pParameter) {
		final TableRow r1 = new TableRow(new RowField($m("PortalTabEditPage.0"), tab_id, tab_name));
		final TableRow r2 = new TableRow(new RowField($m("PortalTabEditPage.1"), tab_description));
		return TableRows.of(r1, r2);
	}
}
