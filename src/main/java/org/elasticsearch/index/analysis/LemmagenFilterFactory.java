package org.elasticsearch.index.analysis;

import eu.hlavki.text.lemmagen.api.Lemmatizer;
import eu.hlavki.text.lemmagen.LemmatizerFactory;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;

import java.io.InputStream;
import java.util.Locale;

public class LemmagenFilterFactory extends AbstractTokenFilterFactory {

    private final Lemmatizer lemmatizer;
    static final String DEFAULT_DIRECTORY = "lemmagen";
    static final String DEFAULT_LEXICON = "ru";

    public LemmagenFilterFactory(IndexSettings indexSettings, String name, Settings settings) {
        super(name, settings);

        String lexicon = settings.get("lexicon", DEFAULT_LEXICON);

        this.lemmatizer = getLemmatizerFromClasspath(lexicon);
    }

    private Lemmatizer getLemmatizerFromClasspath(String lexicon) {
        String filename = lexicon.toLowerCase(Locale.ROOT);
        if (!filename.endsWith(".lem")) {
            filename += ".lem";
        }
        String resourcePath = DEFAULT_DIRECTORY + "/" + filename;

        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException(
                "Lemmagen dictionary not found in plugin resources: " + resourcePath +
                ". Please ensure the .lem file is placed in src/main/resources/lemmagen/ and rebuild the plugin."
            );
        }

        try {
            return LemmatizerFactory.read(is);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load lemmatizer from resource: " + resourcePath, e);
        }
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new LemmagenFilter(tokenStream, lemmatizer);
    }
}