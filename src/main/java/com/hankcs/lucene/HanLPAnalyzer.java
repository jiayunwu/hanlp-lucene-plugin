package com.hankcs.lucene;

import com.hankcs.hanlp.HanLP;
import java.io.Reader;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

public class HanLPAnalyzer extends Analyzer {

    private boolean enablePorterStemming;
    private Set<String> filter;

    /**
     * @param filter 停用词
     * @param enablePorterStemming 是否分析词干（仅限英文）
     */
    public HanLPAnalyzer(Set<String> filter, boolean enablePorterStemming) {
        this.filter = filter;
        this.enablePorterStemming = enablePorterStemming;
    }

    /**
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    public TokenStream tokenStream(String fileName, Reader reader) {
        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment().enableOffset(true), filter, enablePorterStemming,
                reader);
        return new PorterStemFilter(new PorterStemFilter(new StopFilter(enablePorterStemming, tokenizer, filter)));
    }

}
