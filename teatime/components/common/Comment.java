package teatime.components.common;

import teatime.server.TTComponent;

public class Comment implements TTComponent {
	@Override
	public String contentFilter(String content) throws Exception {
		return "";
	}
}
