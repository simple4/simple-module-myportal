package net.simpleframework.module.myportal;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractIdBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalTabBean extends AbstractIdBean {
	private String tabText;

	private ID userId;

	private Date createDate;

	private ETabMark tabMark;

	private int views;

	private String description;

	public String getTabText() {
		return tabText;
	}

	public void setTabText(final String tabText) {
		this.tabText = tabText;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public ETabMark getTabMark() {
		return tabMark == null ? ETabMark.normal : tabMark;
	}

	public void setTabMark(final ETabMark tabMark) {
		this.tabMark = tabMark;
	}

	public int getViews() {
		return views;
	}

	public void setViews(final int views) {
		this.views = views;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	private static final long serialVersionUID = 8142966150870802569L;
}
