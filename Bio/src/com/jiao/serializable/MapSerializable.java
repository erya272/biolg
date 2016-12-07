package com.jiao.serializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;




public class MapSerializable implements Serializable {
	//序列化存储HashMap.对于每一个实体
	//private static Logger logger = LoggerFactory.getLogger(MapSerializable.class);
	
	private static final long serialVersionUID = 11L;
	private HashMap<String, ArrayList<String>> ent = new HashMap<String, ArrayList<String>>();

	
	
	private static HashMap<String, ArrayList<String>> init() {
		Logger.debug("load data from db...", MapSerializable.class);
		MapForEntity mfe = new MapForEntity();
		Logger.debug("load data from db done!", MapSerializable.class);
		return mfe.getData();
	}

	public static void saveMapData(File file, MapSerializable map) {
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(map);
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static MapSerializable loadMapData(File file) {
		long startTime = new Date().getTime();
		Logger.debug("load map data from file...", MapSerializable.class);
		if(file != null){
			if(!file.exists()){
				Logger.debug("can not find map data file...", MapSerializable.class);				
				MapSerializable map = new MapSerializable();
				Logger.debug("save map data to file...", MapSerializable.class);
				saveMapData(file, map);
			}
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(new FileInputStream(file));
				MapSerializable ob = (MapSerializable) is.readObject();
				long endTime = new Date().getTime();
				System.out.println("载入所有点耗费时间" + (endTime - startTime) + " ms!");
				return ob;
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		return null;
	}

	public String getKeyValuePair(String key, String value)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		String s = null;
		if (ent.containsKey(key)) {
			for (String v : ent.get(key)) {
				if (v.split(",")[0].equals(value)) {
					s = key + " " + value;
					System.out.println(Double.valueOf(v.split(",")[1]));
					break;
				}
			}
		}
		return s;
	}

	public Collection<String> getPair(String key) {
		Collection<String> values = new ArrayList<String>();
		Collection<String> tagkey = new ArrayList<String>();
		if (ent.containsKey(key)) {
			values = ent.get(key);
		}
		for (String s : values) {
			// 在这里可以加一个判断条件，比如限定边的score大于某一个值才可以认为这条边是两个点有联系的。
			// 但是这里得使边的score标准化。
//			tagkey.add(s.split(",")[0]);
//			System.out.println(s);
			tagkey.add(s);
		}
		return tagkey;
	}
	public Collection<String> getTargetAndScore(String key) {
		Collection<String> values = new ArrayList<String>();
		Collection<String> tagkey = new ArrayList<String>();
		if (ent.containsKey(key)) {
			values = ent.get(key);
		}
		for (String s : values) {
			// 在这里可以加一个判断条件，比如限定边的score大于某一个值才可以认为这条边是两个点有联系的。
			// 但是这里得使边的score标准化。
			tagkey.add(s);
			//System.out.println(s);
		}
		return tagkey;
	}

	// 构造函数
	public MapSerializable() {
		ent = init();
	}

	public ArrayList<String> ReadAllKey() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(
				new File("D://map.dat")));
		MapSerializable ob = (MapSerializable) is.readObject();
		ArrayList<String> allKey = new ArrayList<String>();
		java.util.Iterator<String> it3 =  ob.ent.keySet().iterator();
		while (it3.hasNext()) {
			String key =  it3.next();
			allKey.add(key);
			//System.out.println(key);
			}
		is.close();
		return allKey;

	}
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		// ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
		// new File("D://map.dat")));
		// os.writeObject(new MapSerializable());
		// os.flush();
		// os.close();

		// ObjectInputStream is = new ObjectInputStream(new FileInputStream(
		// new File("D://map.dat")));
		// MapSerializable ob = (MapSerializable) is.readObject();
		// Iterator<String> it3 = ob.ent.keySet().iterator();
		// while (it3.hasNext()) {
		//
		// String key = (String) it3.next();
		// if (key != null && key.equals("11931")) {
		// for (String s : ob.ent.get(key)) {
		// System.out.println(key + "->" + s);
		// }
		// }
		// }
		// is.close();
		MapSerializable ms = MapSerializable.loadMapData(new File(
				"D://map.dat"));
//		String s = ms.getKeyValuePair("17944", "25095");
//		String s2 = ms.getKeyValuePair("90636","90639" );
//		System.out.println(s);
//		System.out.println(s2);
		ms.getPair("17944");
//		ms.ReadAllKey();
	}
}

