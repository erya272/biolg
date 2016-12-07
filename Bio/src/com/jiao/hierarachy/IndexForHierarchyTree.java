package com.jiao.hierarachy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
//import java.util.Map.Entry;
//import java.util.Iterator;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiao.element.BTree;
import com.jiao.file.FileReadAndWrite;

public class IndexForHierarchyTree {
	// 为层次树做索引，使得每一个父亲节点可以直接找到它的所有叶子节点。
	// 层次聚类树的树状结构的建立
	static FileReadAndWrite fraw = new FileReadAndWrite();
	BTree root = new BTree();
	BTree rootr = new BTree();// 就是找到的那個要插入的點
	static String fr = "E://ScaleFreeNetwork TestData/no weight/100/finalHac2.csv";
	HashMap<String, ArrayList<String>> cleaf = new HashMap<String, ArrayList<String>>();
	static int n = 0;
	static ArrayList<String> Kroot = new ArrayList<String>();

	public HashMap<String, ArrayList<String>> ConstructTreeAndNodeIndex(
			String fr) {
		ArrayList<String> rdata = new ArrayList<String>();
		rdata = fraw.ReadData(fr);
		int N = rdata.size() + 1;
		n = N;
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for (String s : rdata) {
			String clu = s.split(",")[0];
			String n1 = s.split(",")[1];
			String n2 = s.split(",")[2];
			ArrayList<String> values = new ArrayList<String>();
//			if (Integer.valueOf(n1) <= N)
//				values.add(n1);
//			else
//				values.addAll(map.get(n1));
//			if (Integer.valueOf(n2) <= N)
//				values.add(n2);
//			else
//				values.addAll(map.get(n2));
			if (!isNumberic(n1))
				values.add(n1);
			else
				values.addAll(map.get(n1));
			if (!isNumberic(n2))
				values.add(n2);
			else
				values.addAll(map.get(n2));
			map.put(clu, values);
		}
		cleaf = map;
		for (int i = N - 2; i >= 0; i--) {
			String s = rdata.get(i);
			insert(s);
		}

		// levelOrderStack(root);
		return map;
	}

	public void levelOrderStack(BTree p) {
		if (p == null)
			return;
		List<BTree> queue = new LinkedList<BTree>();
		queue.add(p);
		while (!queue.isEmpty()) {
			BTree temp = queue.remove(0);
			System.out.print(temp.name + "," + temp.height);
			System.out.print("\n");
			if (temp.left != null) {
				queue.add(temp.left);
			}
			if (temp.right != null) {
				queue.add(temp.right);
			}
		}
		System.out.println();
	}

	public void insert(String s) {
		String clu = s.split(",")[0];
		String left = s.split(",")[1];
		String right = s.split(",")[2];
		String height = s.split(",")[3];
		if (root.name == null) {
			root = new BTree(clu);
			root.left = new BTree(left);
			root.right = new BTree(right);
			root.height = Double.valueOf(height);
		} else {
			preOrderTravels(root, clu);
			rootr.left = new BTree(left);
			rootr.right = new BTree(right);
			rootr.height = Double.valueOf(height);
		}
	}

	public void preOrderTravels(BTree node, String name) {

		if (node == null) {
			return;
		} else {
			String value = visit(node);
			if (value == name || value.equals(name)) {
				rootr = node;
				return;
			} else {
				preOrderTravels(node.left, name);
				preOrderTravels(node.right, name);
			}
		}
	}

	public String visit(BTree node) {
		if (node.name == null) {
			return null;
		}
		String name = node.name;
		return name;
	}

	public HashMap<String, ArrayList<String>> TreeCutoff(int j, String fr) {
		HashMap<String, ArrayList<String>> hCut = new HashMap<String, ArrayList<String>>();
		ArrayList<String> rdata = new ArrayList<String>();
		rdata = fraw.ReadData(fr);
		int n = rdata.size();
		ArrayList<String> initvalue = new ArrayList<String>();
		initvalue.add(rdata.get(n - j).split(",")[0]);
		hCut.put(1 + "", initvalue);
		preOrderTravels(root, rdata.get(n - j).split(",")[0]);
		root = rootr;
		int count = 1;
		for (int i = n - j; i >= 0; i--) {
			ArrayList<String> resValue = new ArrayList<String>();
			ArrayList<String> lastValue = new ArrayList<String>();
			lastValue = hCut.get(count + "");
			String s = rdata.get(i);
			String n1 = s.split(",")[1];
			String n2 = s.split(",")[2];
			String height = s.split(",")[3];
			if (lastValue != null) {
				for (String ls : lastValue) {
					preOrderTravels(root, ls);
					if (rootr.height < Double.valueOf(height)) {
						resValue.add(ls);
					}
				}
			}
			preOrderTravels(root, n1);
			if (rootr.name == n1 || rootr.name.equals(n1)) {
				resValue.add(n1);
				resValue.add(n2);
				count++;
			}
			if (resValue != null)
				hCut.put(count + "", resValue);
		}

		HashMap<String, ArrayList<String>> map = hCut;
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			ArrayList<String> values = (ArrayList<String>) map.get(key);
			System.out.println(key + "\n" + values);
		}
		return hCut;
	}

	public HashMap<String, ArrayList<String>> TreeCutoffByShort(int j,
			String fr, int layers, ArrayList<String> Kroot) {
		HashMap<String, ArrayList<String>> hCut = new HashMap<String, ArrayList<String>>();
		ArrayList<String> rdata = new ArrayList<String>();
		rdata = fraw.ReadData(fr);
		int n = rdata.size();
		double highheight = Double.valueOf(rdata.get(n - j - 1).split(",")[3]);
		double lowheight = Double.valueOf(rdata.get(0).split(",")[3]);
		double dist = (highheight - lowheight) / layers;

		ArrayList<String> initvalue = new ArrayList<String>();
		initvalue.add(rdata.get(n - j - 1).split(",")[0]);
		hCut.put(0 + "", initvalue);
		int f = 1;
		int cou = 1;
		for (int count = 1; count <= layers; count++) {
			double height = highheight - count * dist;
			if (count == layers)
				height = lowheight;
			ArrayList<String> lastValue = new ArrayList<String>();
			lastValue = hCut.get((cou - 1) + "");
			ArrayList<String> cut = new ArrayList<String>();
			cut.addAll(lastValue);

			for (int i = n - j - f; i >= 0; i--) {
				String s = rdata.get(i);
				String n0 = s.split(",")[0];
				String n1 = s.split(",")[1];
				String n2 = s.split(",")[2];
				String h = s.split(",")[3];
				if (Double.valueOf(h) < height)
					break;
				f++;
				if (cut.contains(n0))
					cut.remove(n0);
				cut.add(n1);
				cut.add(n2);
			}

			if (cut.size() != lastValue.size()) {
				hCut.put(cou + "", cut);
				cou++;
				for (String s : cut) {
					if (!Kroot.contains(s) && Integer.valueOf(s) > n + 1)
						Kroot.add(s);
				}
			}
		}

		HashMap<String, ArrayList<String>> map = hCut;
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			ArrayList<String> values = (ArrayList<String>) map.get(key);
			System.out.println(key + "\n" + values);
		}
		return hCut;
	}

	public HashMap<String, ArrayList<String>> TreeCutoffByDistance(int j,
			String fr, int dis, ArrayList<String> Kroot) {
		HashMap<String, ArrayList<String>> hCut = new HashMap<String, ArrayList<String>>();
		ArrayList<String> rdata = new ArrayList<String>();
		rdata = fraw.ReadData(fr);
		int n = rdata.size();
		int count = 1;
		int f = 1;
		while (n - j + 1 >= count * dis) {
			ArrayList<String> lastValue = new ArrayList<String>();
			lastValue = hCut.get((count - 1) + "");
			ArrayList<String> cut = new ArrayList<String>();
			int m = 1;
			if (lastValue != null) {
				cut.addAll(lastValue);
				for (int i = n - f - j; i >= 0; i--) {
					String s = rdata.get(i);
					String n0 = s.split(",")[0];
					String n1 = s.split(",")[1];
					String n2 = s.split(",")[2];
					if (m > dis)
						break;
					f++;
					if (cut.contains(n0)) {
						cut.add(n1);
						m++;
						cut.add(n2);
						m++;
						cut.remove(n0);
						m--;
					}

				}
				if (cut.size() != lastValue.size()) {
					hCut.put(count + "", cut);
					count++;
					for (String s : cut) {
						// if (!Kroot.contains(s) && Integer.valueOf(s) > n + 1)
						if (!Kroot.contains(s) && isNumberic(s))// 针对以名字作为叶子节点
							Kroot.add(s);
					}
				} else
					break;
			} else {
				for (int i = n - f - j; i >= 0; i--) {
					String s = rdata.get(i);
					String n0 = s.split(",")[0];
					String n1 = s.split(",")[1];
					String n2 = s.split(",")[2];
					if (m > dis)
						break;
					f++;
					if (cut.isEmpty()) {
						cut.add(n1);
						m++;
						cut.add(n2);
						m++;
					} else if (cut.contains(n0)) {
						cut.add(n1);
						m++;
						cut.add(n2);
						m++;
						cut.remove(n0);
						m--;
					}
				}
				hCut.put(count + "", cut);
				count++;
				for (String s : cut) {
					if (!Kroot.contains(s) && isNumberic(s))
						Kroot.add(s);
				}

			}
		}

		if (((count * dis - n + j - 1) > 0 && (count * dis - n + j - 1) <= dis)) {
			// 针对之前有剩余，或是开始总量不够dis
			int m = 0;
			ArrayList<String> lastValue = new ArrayList<String>();
			lastValue = hCut.get((count - 1) + "");
			ArrayList<String> cut = new ArrayList<String>();
			if (lastValue != null)
				cut.addAll(lastValue);
			for (int i = n - f - j; i >= 0; i--) {
				String s = rdata.get(i);
				String n0 = s.split(",")[0];
				String n1 = s.split(",")[1];
				String n2 = s.split(",")[2];
				if (m > dis)
					break;
				f++;
				if (cut.isEmpty()) {
					cut.add(n1);
					m++;
					cut.add(n2);
					m++;
				} else if (cut.contains(n0)) {
					cut.add(n1);
					m++;
					cut.add(n2);
					m++;
					cut.remove(n0);
					m--;
				}
			}
			if (lastValue != null && cut.size() != lastValue.size()) {
				hCut.put(count + "", cut);
				count++;
				for (String s : cut) {
					if (!Kroot.contains(s) && isNumberic(s))
						Kroot.add(s);
				}
			} else if (lastValue == null) {
				hCut.put(count + "", cut);
				count++;
				for (String s : cut) {
					if (!Kroot.contains(s) && isNumberic(s))
						Kroot.add(s);
				}
			}

		}

		 HashMap<String, ArrayList<String>> map = hCut;
		 Set<String> set = map.keySet();
		 Iterator<String> it = set.iterator();
		 while (it.hasNext()) {
		 Object key = it.next();
		 ArrayList<String> values = (ArrayList<String>) map.get(key);
		 System.out.println(key + "\n" + values);
		 }
//		System.out.println(hCut.size());
		return hCut;
	}

	public boolean isNumberic(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		IndexForHierarchyTree ct = new IndexForHierarchyTree();
		// HashMap<String, ArrayList<String>> map =
		// ct.ConstructTreeAndNodeIndex(fr);

		// for (int j = 1; j < n; j++)
		// ct.TreeCutoff(1,fr);
		int n = 5069;
		ArrayList<String> kk = new ArrayList<String>();
		ct.TreeCutoffByDistance(0, fr, 50, kk).size();
		System.out.println("****" + kk);
		for (int j = 1; j < n; j++) {
			if (kk.contains((5070 + n - j) + "")) {
				System.out.println("----" + (5070 + n - j) + "======" + j);
				ct.TreeCutoffByDistance(j, fr, 50, kk);
			}
		}

		// System.out.println(ct.TreeCutoffByDistance(0, fr, 8,
		// kk).get("5").size());
		// System.out.println("-------"+kk.size());
		// for(String s: kk)
		// System.out.println(s);

	}
}
