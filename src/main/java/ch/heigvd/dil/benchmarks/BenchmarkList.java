package ch.heigvd.dil.benchmarks;

import ch.heigvd.dil.utils.parsers.MarkdownParser;
import java.util.concurrent.TimeUnit;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
public class BenchmarkList {

  /**
   * From the command line: $ mvn clean install $ java -jar target/benchmarks.jar BenchmarkList
   *
   * @param args
   * @throws RunnerException
   */
  public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(BenchmarkList.class.getSimpleName())
            .forks(0)
            .mode(Mode.SingleShotTime)
            .warmupIterations(0)
            .measurementIterations(1)
            .build();
    new Runner(opt).run();
  }

  @Param({"10", "100", "1000", "10000", "100000"})
  public int size;

  public Parser parser;

  @Setup
  public void setup() {
    parser = Parser.builder().build();
  }

  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  @Warmup(iterations = 3)
  @Measurement(iterations = 10)
  public void build() {
    for (int i = 0; i < size; i++) {
      // simple appel du markdown parser
      MarkdownParser.convertMarkdownToHTML("#titre");
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  @Warmup(iterations = 3)
  @Measurement(iterations = 10)
  public void buildOptimized() {

    for (int i = 0; i < size; i++) {
      // appel du markdown parser en réutilisant le même parser à chaque fois
      Node document = parser.parse("#titre");
      HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
      htmlRenderer.render(document);
    }
  }
}
