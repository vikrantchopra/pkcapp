package com.perceptive.epm.perkolcentral.common.utils;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.core.SimpleAnalyzer;
//import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10/9/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LuceneUtil {

    private RAMDirectory ramDirectory;
    IndexWriter indexWriter;
    IndexSearcher indexSearcher;


    public RAMDirectory getRamDirectory() {
        return ramDirectory;
    }

    public void setRamDirectory(RAMDirectory ramDirectory) {
        this.ramDirectory = ramDirectory;
    }

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public void setIndexSearcher(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }


    public void indexUserInfo(Collection<EmployeeBO> employeeBOs) throws ExceptionWrapper {
        try {
            ramDirectory = new RAMDirectory();
            /*Create instance of analyzer, which will be used to tokenize
         the input data */
            Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_36);
            Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_36);
            Analyzer whiteSpaceAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_36);


//          Create the instance of deletion policy
            IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_36, standardAnalyzer));
            for (EmployeeBO employeeBO : employeeBOs) {
                Field employeeNameField = new Field(Constants.employeeName, employeeBO.getEmployeeName(), Field.Store.YES,
                        Field.Index.ANALYZED);
                Field employeeUIDField = new Field(Constants.employeeUID, employeeBO.getEmployeeUid(), Field.Store.YES,
                        Field.Index.NOT_ANALYZED);
                Field employeeID = new Field(Constants.employeeID, employeeBO.getEmployeeId(), Field.Store.YES,
                        Field.Index.NOT_ANALYZED);
                Document doc = new Document();
                doc.add(employeeNameField);
                doc.add(employeeUIDField);
                doc.add(employeeID);
                indexWriter.addDocument(doc);
            }
            //indexWriter.optimize();
            indexWriter.close();

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public ArrayList<String> getBestMatchEmployeeName(String searchString) throws ExceptionWrapper {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        try {
            indexSearcher = new IndexSearcher(IndexReader.open(ramDirectory));
            Query query = query = new FuzzyQuery(new Term(Constants.employeeName, searchString.toLowerCase()));
            TopDocs topDocs = indexSearcher.search(query, 100);
            if (topDocs.scoreDocs.length <= 0) {
                return stringArrayList;
            }
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);
                stringArrayList.add(doc.get(Constants.employeeID));
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return stringArrayList;
    }
}
