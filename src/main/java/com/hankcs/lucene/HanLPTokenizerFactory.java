package com.hankcs.lucene;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Tokenizer;

public class HanLPTokenizerFactory extends AbstractAnalysisFactory {

    private static final Pattern ITEM_PATTERN = Pattern.compile("[^,\\s]+");
    private boolean enableIndexMode;
    private boolean enablePorterStemming;
    private boolean enableNumberQuantifierRecognize;
    private boolean enableCustomDictionary;
    private boolean enableTranslatedNameRecognize;
    private boolean enableJapaneseNameRecognize;
    private boolean enableOrganizationRecognize;
    private boolean enablePlaceRecognize;
    private boolean enableNameRecognize;
    private boolean enableTraditionalChineseMode;
    private Set<String> stopWordDictionary;

    /**
     * 初始化工厂类
     *
     * @param args 通过这个Map保存xml中的配置项
     */
    public HanLPTokenizerFactory(Map<String, String> args) {
        if (args == null) {
            args = new HashMap<String, String>();
        }
        enableIndexMode = getBoolean(args, "enableIndexMode", true);
        enablePorterStemming = getBoolean(args, "enablePorterStemming", false);
        enableNumberQuantifierRecognize = getBoolean(args, "enableNumberQuantifierRecognize", false);
        enableCustomDictionary = getBoolean(args, "enableCustomDictionary", true);
        enableTranslatedNameRecognize = getBoolean(args, "enableTranslatedNameRecognize", false);
        enableJapaneseNameRecognize = getBoolean(args, "enableJapaneseNameRecognize", false);
        enableOrganizationRecognize = getBoolean(args, "enableOrganizationRecognize", false);
        enableNameRecognize = getBoolean(args, "enableNameRecognize", false);
        enablePlaceRecognize = getBoolean(args, "enablePlaceRecognize", false);
        enableTraditionalChineseMode = getBoolean(args, "enableTraditionalChineseMode", false);
        HanLP.Config.Normalization = getBoolean(args, "enableNormalization", HanLP.Config.Normalization);
        Set<String> customDictionaryPathSet = getSet(args, "customDictionaryPath");
        if (customDictionaryPathSet != null) {
            HanLP.Config.CustomDictionaryPath = customDictionaryPathSet.toArray(new String[0]);
        }
        String stopWordDictionaryPath = get(args, "stopWordDictionaryPath");
        if (stopWordDictionaryPath != null) {
            stopWordDictionary = new TreeSet<String>();
            stopWordDictionary.addAll(IOUtil.readLineListWithLessMemory(stopWordDictionaryPath));
        }
        if (getBoolean(args, "enableDebug", false)) {
            HanLP.Config.enableDebug();
        }
    }

    public Tokenizer create(Reader reader) {
        Segment segment = HanLP.newSegment().enableOffset(true).enableIndexMode(enableIndexMode)
                .enableNameRecognize(enableNameRecognize)
                .enableNumberQuantifierRecognize(enableNumberQuantifierRecognize)
                .enableCustomDictionary(enableCustomDictionary)
                .enableTranslatedNameRecognize(enableTranslatedNameRecognize)
                .enableJapaneseNameRecognize(enableJapaneseNameRecognize)
                .enableOrganizationRecognize(enableOrganizationRecognize)
                .enablePlaceRecognize(enablePlaceRecognize);
        if (enableTraditionalChineseMode) {
            segment.enableIndexMode(false);
            Segment inner = segment;
            TraditionalChineseTokenizer.SEGMENT = inner;
            segment = new Segment() {
                @Override
                protected List<Term> segSentence(char[] sentence) {
                    List<Term> termList = TraditionalChineseTokenizer.segment(new String(sentence));
                    return termList;
                }
            };
        }

        return new HanLPTokenizer(segment, stopWordDictionary, enablePorterStemming, reader);
    }


}
