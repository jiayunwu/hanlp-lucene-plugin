package com.hankcs.lucene;

import com.hankcs.hanlp.HanLP;
import java.io.Reader;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

public class HanLPIndexAnalyzer extends Analyzer {

    private boolean pstemming;
    private Set<String> filter;

    /**
     * @param filter 停用词
     * @param pstemming 是否分析词干
     */
    public HanLPIndexAnalyzer(Set<String> filter, boolean pstemming) {
        this.filter = filter;
        this.pstemming = pstemming;
    }

    /**
     * @param pstemming 是否分析词干.进行单复数,时态的转换
     */
    public HanLPIndexAnalyzer(boolean pstemming) {
        this.pstemming = pstemming;
    }

    public HanLPIndexAnalyzer() {
        super();
    }

    @Override
    public TokenStream tokenStream(String FileName, Reader reader) {
        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment().enableIndexMode(true), filter, pstemming, reader);
        return new PorterStemFilter(new PorterStemFilter(new StopFilter(pstemming, tokenizer, filter)));
    }

}
