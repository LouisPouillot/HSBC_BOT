package ml;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;

import javax.xml.bind.SchemaOutputResolver;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by louis on 12/01/17.
 */
public class Intent implements Serializable{

    private int label;
    private List<String> utterances;
    private String response;
    private Boolean isFeedbackIntentYes;
    private Boolean isFeedbackIntentNo;

    private JavaRDD<LabeledPoint> rdd;

    @JsonCreator
    public Intent( @JsonProperty("label") int label,
                   @JsonProperty("response") String response,
                   @JsonProperty("feedback_no") Boolean isFeedbackIntentNo,
                   @JsonProperty("feedback_yes") Boolean isFeedbackIntentYes,
                   @JsonProperty("utterances") List<String> utterances){
        System.out.println("NEW INTENT, LABEL:" + label +" RESPONCE: "+ response);
        this.label = label - 1;
        this.response = response;
        this.utterances = utterances;
        this.isFeedbackIntentNo = isFeedbackIntentNo;
        this.isFeedbackIntentYes = isFeedbackIntentYes;
    }

    /** Each question is split into words, and each word is mapped to one feature.
         Create LabeledPoint datasets for each intent**/
    public void transformToPoints(final HashingTF tf, JavaSparkContext sparkContext){
        JavaRDD<String> rdd_string = sparkContext.parallelize(utterances);

        this.rdd = rdd_string.map(new Function<String, LabeledPoint>() {
            public LabeledPoint call(String question) {
                return new LabeledPoint(label*1.0, tf.transform(Arrays.asList(question.split(" "))));
            }
        });
        System.out.println("Question with label "+label+" has been transfromed");
    }

    public JavaRDD<LabeledPoint> getRdd() {
        return rdd;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public List<String> getUtterances() {
        return utterances;
    }

    public void setUtterances(List<String> utterances) {
        this.utterances = utterances;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getIsFeedbackIntentYes() {
        return isFeedbackIntentYes;
    }

    public void setIsFeedbackIntentYes(Boolean isFeedbackIntentYes) {
        this.isFeedbackIntentYes = isFeedbackIntentYes;
    }

    public Boolean getIsFeedbackIntentNo() {
        return isFeedbackIntentNo;
    }

    public void setIsFeedbackIntentNo(Boolean isFeedbackIntentNo) {
        this.isFeedbackIntentNo = isFeedbackIntentNo;
    }
}
