package schwarzer.shelves;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charlotte.tools.FileTools;
import charlotte.tools.MapTools;
import charlotte.tools.ReflecTools;
import charlotte.tools.RunnableEx;
import charlotte.tools.StringTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;

public abstract class ShelvesDesign extends ShelvesManager {
	private XNode root;

	public ShelvesDesign(XNode root) throws Exception {
		this.root = root;
		init();
	}

	private static XFormat format = null;

	private void init() throws Exception {
		if(format == null) {
			format = new XFormat(XNode.load(FileTools.readToEnd(ShelvesDesign.class.getResource(
					"format/Design.xml"
					))));
		}
		format.check(root);
	}

	private Form form = null;

	@Override
	public Form getForm() {
		try {
			if(form == null) {
				createForm();
			}
			return form;
		}
		catch(Exception e) {
			throw RunnableEx.re(e);
		}
	}

	private void createForm() throws Exception {
		Map<String, String> props = MapTools.create();

		form = new Form();

		for(XNode node : root.getNodes("Header")) {
			form.header = new Header();
			props.clear();

			addToProps(props, root.getNodes("Default/Header"));
			addToProps(props, node);
			ignoreProp(props, "Default");
			ignoreProp(props, "Button");

			setProps(form.header, props);
		}
		for(XNode node : root.getNodes("Header/Button")) {
			Button button = new Button();
			props.clear();

			addToProps(props, root.getNodes("Default/Button"));
			addToProps(props, root.getNodes("Header/Default/Button"));
			addToProps(props, node);

			setProps(button, props);
			form.header.buttons.add(button);
		}
		for(XNode node : root.getNodes("Footer")) {
			form.footer = new Header();
			props.clear();

			addToProps(props, root.getNodes("Default/Footer"));
			addToProps(props, node);
			ignoreProp(props, "Default");
			ignoreProp(props, "Button");

			setProps(form.footer, props);
		}
		for(XNode node : root.getNodes("Footer/Button")) {
			Button button = new Button();
			props.clear();

			addToProps(props, root.getNodes("Default/Button"));
			addToProps(props, root.getNodes("Footer/Default/Button"));
			addToProps(props, node);

			setProps(button, props);
			form.footer.buttons.add(button);
		}
		for(XNode tabNode : root.getNodes("Tab")) {
			Tab tab = new Tab();
			props.clear();

			addToProps(props, root.getNodes("Default/Tab"));
			addToProps(props, tabNode);
			ignoreProp(props, "Default");
			ignoreProp(props, "Column");

			setProps(tab, props);
			form.tabs.add(tab);

			for(XNode columnNode : tabNode.getNodes("Column")) {
				Column column = new Column();
				props.clear();

				addToProps(props, root.getNodes("Default/Column"));
				addToProps(props, tabNode.getNodes("Default/Column"));
				addToProps(props, columnNode);
				ignoreProp(props, "Default");
				ignoreProp(props, "Shelf");

				setProps(column, props);
				tab.columns.add(column);

				for(XNode shelfNode : columnNode.getNodes("Shelf")) {
					String lClassName = shelfNode.getNodeValue("className");
					String className = this.getShelfPackage().getName() + "." + lClassName;
					Shelf shelf = (Shelf)ReflecTools.invokeDeclaredCtor(className, new Object[0]);
					props.clear();

					addToProps(props, root.getNodes("Default/Shelf"));
					addToProps(props, tabNode.getNodes("Default/Shelf"));
					addToProps(props, columnNode.getNodes("Default/Shelf"));
					addToProps(props, matchedClassNameOnly(root.getNodes("Default/NShelf"), lClassName));
					addToProps(props, matchedClassNameOnly(tabNode.getNodes("Default/NShelf"), lClassName));
					addToProps(props, matchedClassNameOnly(columnNode.getNodes("Default/NShelf"), lClassName));
					addToProps(props, shelfNode);
					ignoreProp(props, "className");

					setProps(shelf, props);
					column.shelves.add(shelf);
				}
			}
		}
	}

	private List<XNode> matchedClassNameOnly(List<XNode> src, String className) {
		List<XNode> dest = new ArrayList<XNode>();

		for(XNode node : src) {
			if(className.equals(node.getNodeValue("className"))) {
				dest.add(node);
			}
		}
		return dest;
	}

	private void addToProps(Map<String, String> dest, List<XNode> nodes) {
		for(XNode node : nodes) {
			addToProps(dest, node);
		}
	}

	private void addToProps(Map<String, String> dest, XNode node) {
		for(XNode prop : node.getChildren()) {
			dest.put(prop.getName(), prop.getValue());
		}
	}

	private void ignoreProp(Map<String, String> props, String name) {
		props.remove(name);
	}

	private void setProps(Object dest, Map<String, String> props) throws Exception {
		for(String name : props.keySet()) {
			Field field = ReflecTools.getField(dest.getClass(), name);
			Object value = props.get(name);

			if(boolean.class.equals(field.getType())) {
				value = new Boolean(StringTools.toFlag("" + value));
			}
			else if(int.class.equals(field.getType())) {
				value = new Integer(Integer.parseInt("" + value));
			}
			else if(long.class.equals(field.getType())) {
				value = new Long(Long.parseLong("" + value));
			}
			else if(double.class.equals(field.getType())) {
				value = new Double(Double.parseDouble("" + value));
			}
			ReflecTools.setObject(field, dest, value);
		}
	}

	private String valToStrInt(Object value) {
		String ret = "" + value;

		ret = StringTools.replaceChar(ret, StringTools.ZEN_ALPHA, StringTools.ALPHA);
		ret = StringTools.replaceChar(ret, StringTools.zen_alpha, StringTools.alpha);
		ret = StringTools.replaceChar(ret, StringTools.ZEN_DIGIT, StringTools.DIGIT);
		ret = StringTools.replaceChar(ret, StringTools.ZEN_PUNCT, StringTools.PUNCT);
		ret = StringTools.trim(ret);

		return ret;
	}

	public List<Shelf> getAllShelf() {
		List<Shelf> ret = new ArrayList<Shelf>();

		for(Tab tab : getForm().tabs) {
			for(Column column : tab.columns) {
				for(Shelf shelf : column.shelves) {
					ret.add(shelf);
				}
			}
		}
		return ret;
	}
}
