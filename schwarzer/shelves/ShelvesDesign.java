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
		Map<String, XNode> props = MapTools.<XNode>create();

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
					addToProps(props, onlyClassName(root.getNodes("Default/NShelf"), lClassName));
					addToProps(props, onlyClassName(tabNode.getNodes("Default/NShelf"), lClassName));
					addToProps(props, onlyClassName(columnNode.getNodes("Default/NShelf"), lClassName));
					addToProps(props, shelfNode);
					ignoreProp(props, "className");

					setProps(shelf, props);
					column.shelves.add(shelf);
				}
			}
		}
	}

	private List<XNode> onlyClassName(List<XNode> src, String className) {
		List<XNode> dest = new ArrayList<XNode>();

		for(XNode node : src) {
			if(className.equals(node.getNodeValue("className"))) {
				dest.add(node);
			}
		}
		return dest;
	}

	private void addToProps(Map<String, XNode> dest, List<XNode> nodes) {
		for(XNode node : nodes) {
			addToProps(dest, node);
		}
	}

	private void addToProps(Map<String, XNode> dest, XNode node) {
		for(XNode prop : node.getChildren()) {
			dest.put(prop.getName(), prop);
		}
	}

	private void ignoreProp(Map<String, XNode> props, String name) {
		props.remove(name);
	}

	private static final String MAP_KEY_NODE_NAME = "name";
	private static final String MAP_VALUE_NODE_NAME = "value";

	private void setProps(Object dest, Map<String, XNode> props) throws Exception {
		for(String name : props.keySet()) {
			Field field = ReflecTools.getField(dest.getClass(), name);
			Class<?> fieldType = field.getType();

			if(List.class.equals(fieldType)) {
				List<String> val = new ArrayList<String>();

				for(XNode node : props.get(name).getChildren()) {
					val.add(node.getValue());
				}
				ReflecTools.setObject(field, dest, val);
			}
			else if(Map.class.equals(fieldType)) {
				Map<String, String> val = MapTools.create();

				for(XNode node : props.get(name).getChildren()) {
					if(node.hasNode(MAP_KEY_NODE_NAME) && node.hasNode(MAP_VALUE_NODE_NAME)) {
						val.put(node.getNodeValue(MAP_KEY_NODE_NAME), node.getNodeValue(MAP_VALUE_NODE_NAME));
					}
					else {
						val.put(node.getName(), node.getValue());
					}
				}
				ReflecTools.setObject(field, dest, val);
			}
			else {
				Object value = props.get(name).getValue();

				if(boolean.class.equals(fieldType)) {
					value = new Boolean(StringTools.toFlag("" + value));
				}
				else if(int.class.equals(fieldType)) {
					value = new Integer(Integer.parseInt("" + value));
				}
				else if(long.class.equals(fieldType)) {
					value = new Long(Long.parseLong("" + value));
				}
				else if(double.class.equals(fieldType)) {
					value = new Double(Double.parseDouble("" + value));
				}
				else if(String.class.equals(fieldType)) {
					// noop
				}
				else {
					throw new Exception("クラス [" + dest.getClass() + "] の、フィールド [" + field.getName() + "] の型に問題があります。");
				}
				ReflecTools.setObject(field, dest, value);
			}
		}
	}

	// いるかしら..
	/*
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
	*/
}
