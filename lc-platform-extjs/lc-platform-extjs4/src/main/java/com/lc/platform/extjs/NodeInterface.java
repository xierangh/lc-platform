package com.lc.platform.extjs;

/**
 * tree节点数据
 * 
 * @author chenjun
 *
 */
public class NodeInterface {

	/**
	 * Set to false to deny dragging of this node.<br/>
	 * Defaults to: true
	 */
	protected boolean allowDrag = true;
	/**
	 * Set to false to deny dropping on this node.<br/>
	 * Defaults to: true
	 */
	protected boolean allowDrop = true;

	/**
	 * Set to true or false to show a checkbox alongside this node.<br/>
	 * Defaults to: null
	 */
	protected Boolean checked;
	/**
	 * Array of child nodes.
	 */
	protected NodeInterface children;

	/**
	 * Set to true to indicate that this child can have no children. The expand
	 * icon/arrow will then not be rendered for this node.<br/>
	 * Defaults to: false
	 */
	protected boolean leaf = false;
	/**
	 * The text to show on node label.
	 */
	protected String text;
	/**
	 * ID of parent node.
	 */
	protected String parentId;

	/**
	 * URL for this node's icon.
	 */
	protected String icon;

	public boolean isAllowDrag() {
		return allowDrag;
	}

	public void setAllowDrag(boolean allowDrag) {
		this.allowDrag = allowDrag;
	}

	public boolean isAllowDrop() {
		return allowDrop;
	}

	public void setAllowDrop(boolean allowDrop) {
		this.allowDrop = allowDrop;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public NodeInterface getChildren() {
		return children;
	}

	public void setChildren(NodeInterface children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
