package net.simpleframework.module.myportal;

import java.io.Reader;

import net.simpleframework.common.bean.AbstractIdBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LayoutLobBean extends AbstractIdBean {

	private Reader layoutLob;

	public Reader getLayoutLob() {
		return layoutLob;
	}

	public void setLayoutLob(final Reader layoutLob) {
		this.layoutLob = layoutLob;
	}

	private static final long serialVersionUID = 6391781294365189653L;
}
