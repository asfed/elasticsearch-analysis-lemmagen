package org.elasticsearch.plugin.analysis.lemmagen;

import org.elasticsearch.index.analysis.LemmagenFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Collections;
import java.util.Map;

public class AnalysisLemmagenPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap(
            "lemmagen",
            (indexSettings, environment, name, settings) -> 
                new LemmagenFilterFactory(indexSettings, name, settings)
        );
    }
}