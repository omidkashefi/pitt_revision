package edu.pitt.lrdc.cs.revision.discourse;

import scala.Option;
import edu.arizona.sista.struct.DirectedGraphEdgeIterator;
import edu.arizona.sista.processors.*;
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor;
import edu.arizona.sista.processors.fastnlp.FastNLPProcessor;

public class Test {
	public static void main(String[] args) throws Exception {
		// create the processor
		// Processor proc = new FastNLPProcessor(true, false, true, 100);
		Processor proc = new FastNLPProcessor(false, false, true, true);
		// for much faster processing, use FastNLPProcessor
		// Processor proc = new FastNLPProcessor(true, false);

		// the actual work is done here
		Document doc = proc
				.annotate("John Smith went to China and he visited Beijing, on January 10th, 2013. Then he went to Japan. Japan is a country in the east of China. He also went to Korea");
		proc.discourse(doc);
		// you are basically done. the rest of this code simply prints out the
		// annotations

		// let's print the sentence-level annotations
		int sentenceCount = 0;
		for (Sentence sentence : doc.sentences()) {
			System.out.println("Sentence #" + sentenceCount + ":");
			Option<String[]> oo = sentence.chunks();
			String[] chunks = oo.get();
			System.out.println("Chunks: " + mkString(chunks, " "));
			System.out.println("Tokens: " + mkString(sentence.words(), " "));
			System.out.println("Start character offsets: "
					+ mkString(sentence.startOffsets(), " "));
			System.out.println("End character offsets: "
					+ mkString(sentence.endOffsets(), " "));

			// these annotations are optional, so they are stored using Option
			// objects, hence the isDefined checks
			if (sentence.lemmas().isDefined()) {
				System.out.println("Lemmas: "
						+ mkString(sentence.lemmas().get(), " "));
			}
			if (sentence.tags().isDefined()) {
				System.out.println("POS tags: "
						+ mkString(sentence.tags().get(), " "));
			}
			if (sentence.entities().isDefined()) {
				System.out.println("Named entities: "
						+ mkString(sentence.entities().get(), " "));
			}
			if (sentence.norms().isDefined()) {
				System.out.println("Normalized entities: "
						+ mkString(sentence.norms().get(), " "));
			}
			if (sentence.dependencies().isDefined()) {
				System.out.println("Syntactic dependencies:");
				DirectedGraphEdgeIterator<String> iterator = new DirectedGraphEdgeIterator<String>(
						sentence.dependencies().get());
				while (iterator.hasNext()) {
					scala.Tuple3<Object, Object, String> dep = iterator.next();
					// note that we use offsets starting at 0 (unlike CoreNLP,
					// which uses offsets starting at 1)
					System.out.println(" head:" + dep._1() + " modifier:"
							+ dep._2() + " label:" + dep._3());
				}
			}
			if (sentence.syntacticTree().isDefined()) {
				System.out.println("Constituent tree: "
						+ sentence.syntacticTree().get());
				// see the edu.arizona.sista.struct.Tree class for more
				// information
				// on syntactic trees, including access to head phrases/words
			}

			sentenceCount += 1;
			System.out.println("\n");
		}

		// let's print the coreference chains
		if (doc.coreferenceChains().isDefined()) {
			// these are scala.collection Iterator and Iterable (not Java!)
			scala.collection.Iterator<scala.collection.Iterable<CorefMention>> chains = doc
					.coreferenceChains().get().getChains().iterator();
			while (chains.hasNext()) {
				scala.collection.Iterator<CorefMention> chain = chains.next()
						.iterator();
				System.out
						.println("Found one coreference chain containing the following mentions:");
				while (chain.hasNext()) {
					CorefMention mention = chain.next();
					// note that all these offsets start at 0 too
					System.out.println("\tsentenceIndex:"
							+ mention.sentenceIndex() + " headIndex:"
							+ mention.headIndex() + " startTokenOffset:"
							+ mention.startOffset() + " endTokenOffset:"
							+ mention.endOffset());
				}
			}
		}
	}

	public static String mkString(String[] sa, String sep) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			if (i > 0)
				os.append(sep);
			os.append(sa[i]);
		}
		return os.toString();
	}

	public static String mkString(int[] sa, String sep) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			if (i > 0)
				os.append(sep);
			os.append(Integer.toString(sa[i]));
		}
		return os.toString();
	}
}
