package com.concordia.research.NiCad.HTML.results;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseNiCadHTMLResults {
	static ArrayList<String> FindCloneInSameCode = new ArrayList<>();
	static boolean isInSameFile = false;

	public static void main(String[] args) throws IOException {
		Document htmlFile = null;
		String SplitCaption[] = null;
		String CloneClassType = null;
		String CloneSizeByLines = null;
		String Similarity = "";
		FileWriter writer = new FileWriter("/Users/manikhossain/AllAnswersBlocksClone.csv");
		String CSVbuilder = null;
		String postId = null;
		String codeSnippetsId_ = null;
		String postIdWithSnippetNo = null;
		// String LinesFromTo = null;
		int codeLengthInChar = 0;
		int id = 0;
		
		try {
			htmlFile = Jsoup.parse(new File(
					"/Users/manikhossain/Downloads/NiCad-5.2/examples/AnswersCloneBlock_blocks-blind-clones/"
							+ "AnswersCloneBlock_blocks-blind-clones-0.30-classes-withsource.html"),
					"ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements table = htmlFile.select("table"); // select the first table.
		for (int i = 0; i < table.size(); i++) { // get all the table list. table.size();
			Element h3caption = table.get(i).previousElementSibling();
			SplitCaption = h3caption.toString().split(",");
			CloneClassType = SplitCaption[0].split(" ")[2];
			CloneSizeByLines = SplitCaption[2].split(" ")[3];
			Similarity = SplitCaption[3].split(" ")[2].split("%")[0];
			Elements rows = table.get(i).select("tr");
			for (int j = 0; j < rows.size(); j++) { // first row is the col names so skip it. rows.size()
				Element row = rows.get(j);
				Elements cols = row.select("td");
				if (cols.toString().split("<pre>").length > 1) {
					String[] filterString = cols.toString().split("<pre>");
					postId = filterString[0].split("/")[4].split("_")[0].strip();
					codeSnippetsId_ = filterString[0].split("/")[4].split("_")[1].split("\\.")[0].strip();
					postIdWithSnippetNo = filterString[0].split("/")[4].split("\\.")[0].strip();
					isInSameFile = false;
					//findCloneInSameFile(postIdWithSnippetNo, postId);
					// LinesFromTo = filterString[0].split("of")[0].replace("Lines",
					// "").replace("<td>", "").strip();
					codeLengthInChar = filterString[1].strip().split("</pre>")[0].length();

				}
				if (!isInSameFile) {
					id += 1;
					//if(postId.contentEquals("FoundProblemInData")) 
					CSVbuilder = id + "," + postId +"," + codeSnippetsId_+ "," + CloneClassType + "," + CloneSizeByLines + "," + Similarity + ","
							+ codeLengthInChar;
					writer.write("\n");
					writer.write(CSVbuilder);
					System.out.println("Total: " + id);
				}
			}
			h3caption = h3caption.empty();
			FindCloneInSameCode.clear();
		}
		writer.close();
		System.out.println("Import finished.....");

	}

	private static boolean findCloneInSameFile(String postIdWithSnippetNo, String postId) {
		isInSameFile = false;
		for (String clonedPostidWithSnippets : FindCloneInSameCode) {
			if (postId.contains(clonedPostidWithSnippets)) {
				isInSameFile = true;
				break;
			}
		}
		FindCloneInSameCode.add(postIdWithSnippetNo);
		return isInSameFile;
	}

}
