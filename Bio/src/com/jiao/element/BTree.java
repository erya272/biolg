package com.jiao.element;

public class BTree {
	public String name;
	public BTree left;
	public BTree right;
	public Boolean flag;
	public double height;
	public BTree() {
	}
	public BTree(String name) {
		this.name = name;
		this.left = null;
		this.right = null;
		this.flag = false;
		this.height = 0;
	}

	public String getNode() {
		return name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public void setNode(String node) {
		this.name = node;
	}

	public BTree getLeft() {
		return left;
	}

	public void setLeft(BTree left) {
		this.left = left;
	}

	public BTree getRight() {
		return right;
	}

	public void setRight(BTree right) {
		this.right = right;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

}
