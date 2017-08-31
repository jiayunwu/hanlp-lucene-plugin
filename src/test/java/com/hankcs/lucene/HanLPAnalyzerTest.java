package com.hankcs.lucene;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class HanLPAnalyzerTest extends TestCase {

    public void testCreateComponents() throws Exception {
        String content = "亚太地区最具影响的经济合作官方论坛。截至2013年9月，亚太经合组织共有21个正式成员和三个观察员。";
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream(content.getBytes()));
        Set<String> filer = new HashSet<String>();
        HanLPAnalyzer analyzer = new HanLPAnalyzer(filer, false);
        TokenStream tokenStream = analyzer.tokenStream("", input);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            TermAttribute attribute = tokenStream.getAttribute(TermAttribute.class);
            // 偏移量
            OffsetAttribute offsetAtt = tokenStream.getAttribute(OffsetAttribute.class);
            // 距离
            PositionIncrementAttribute positionAttr = tokenStream.getAttribute(PositionIncrementAttribute.class);
            // 词性
            TypeAttribute typeAttr = tokenStream.getAttribute(TypeAttribute.class);
            System.out.printf("[%d:%d %d] %s/%s\n", offsetAtt.startOffset(), offsetAtt.endOffset(),
                    positionAttr.getPositionIncrement(), attribute, typeAttr.type());
        }
    }

}