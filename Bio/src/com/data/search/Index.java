package com.data.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.jiao.file.FileReadAndWrite;

//对每一个聚类里面的节点做索引
public class Index {
	static String path = "E://ScaleFreeNetWork TestData/1000/FixedR/SN/";
	static String InNodesfr = "E://ScaleFreeNetWork TestData/1000/FixedR/IndependentNodes.csv";
	static FileReadAndWrite fraw = new FileReadAndWrite();

	public HashMap<String, ArrayList<String>> getAllClusterNode(String snpath,
			String infr) {
		File file = new File(snpath);
		File[] tempList = file.listFiles();
		int len = tempList.length;
		HashMap<String, ArrayList<String>> Snn = new HashMap<String, ArrayList<String>>();
		for (int i = 0; i < len; i++) {
			ArrayList<String> cnode = new ArrayList<String>();
			cnode = fraw.ReadData(tempList[i].toString());
			String sn = tempList[i].getName().split("\\.")[0];
			Snn.put(sn, cnode);
		}
		ArrayList<String> innodes = new ArrayList<String>();
		innodes = fraw.ReadData(infr);
		for (String s : innodes) {
			ArrayList<String> ss = new ArrayList<String>();
			ss.add(s);
			Snn.put(s, ss);
		}
		return Snn;
	}

	public void MakeNodeIndex() {
		long startTime = new Date().getTime();
		try {
			File indexDir = new File(
					"E://ScaleFreeNetWork TestData/1000/FixedR/NodeIndex/");
			Directory dir = FSDirectory.open(indexDir);
			Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_47);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47,
					luceneAnalyzer);
			File[] files = indexDir.listFiles();
			if (files.length == 0)
				iwc.setOpenMode(OpenMode.CREATE);
			else
				iwc.setOpenMode(OpenMode.APPEND);
			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			HashMap<String, ArrayList<String>> Snn = getAllClusterNode(path,
					InNodesfr);
			Set<Entry<String, ArrayList<String>>> set = Snn.entrySet();
			Iterator<Entry<String, ArrayList<String>>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, ArrayList<String>> k = it.next();
				String sn = k.getKey();
				ArrayList<String> nn = k.getValue();
				int nnlen = nn.size();
				for (String s : nn) {
					Document doc = new Document();
					Field normalnode = new StringField("normal-node", s,
							Store.YES);
					Field supernode = new StringField("super-node", sn,
							Store.YES);
					Field tag;
					if (nnlen == 1) {
						tag = new StringField("tag", "I", Store.YES);
					} else {
						tag = new StringField("tag", "S", Store.YES);
					}
					doc.add(normalnode);
					doc.add(supernode);
					doc.add(tag);
					indexWriter.addDocument(doc);
				}
			}
			indexWriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		long endTime = new Date().getTime();
		System.out.println("该文档花费" + (endTime - startTime) + " ms去建立索引!");

	}

	public String Searcher(String queryString) {
		String index = "E://ScaleFreeNetWork TestData/1000/FixedR/NodeIndex/";
		IndexReader reader;
		String ns = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(new File(
					index)));
			IndexSearcher searcher = new IndexSearcher(reader);
			ScoreDoc[] hits = null;
			TermQuery query = new TermQuery(new Term("normal-node", queryString));

			if (searcher != null) {
				TopDocs results = searcher.search(query, 1);
				hits = results.scoreDocs;
				if (hits.length > 0) {
					int num = hits[0].doc;
					Document document = searcher.doc(num);
					String sn = document.get("super-node");
					String tag = document.get("tag");
					ns = sn+","+tag;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ns;
	}

	public static void main(String[] args) throws IOException {
		Index ind = new Index();
//		ind.MakeNodeIndex();
		System.out.println(ind.Searcher("42"));
	}
}
