package software.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.cloudwatchlogs.emf.exception.DimensionSetExceededException;
import software.amazon.cloudwatchlogs.emf.exception.InvalidDimensionException;
import software.amazon.cloudwatchlogs.emf.exception.InvalidMetricException;
import software.amazon.cloudwatchlogs.emf.logger.MetricsLogger;
import software.amazon.cloudwatchlogs.emf.model.DimensionSet;
import software.amazon.cloudwatchlogs.emf.model.StorageResolution;
import software.amazon.cloudwatchlogs.emf.model.Unit;

import java.util.HashMap;
import java.util.Map;

public class HandlerWithNoPriming implements RequestHandler<Map<String, String>, String> {

    private final MetricsLogger metricsLogger = new MetricsLogger();
    private final Logger logger = LoggerFactory.getLogger(HandlerWithNoPriming.class);

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        try {
            metricsLogger.putDimensions(DimensionSet.of("Service", "Aggregator"));
            metricsLogger.putMetric("ProcessingLatency", 100, Unit.MILLISECONDS);
            metricsLogger.putMetric("CPU Utilization", 87, Unit.PERCENT, StorageResolution.HIGH);
        } catch (InvalidDimensionException | InvalidMetricException | DimensionSetExceededException e) {
            logger.error(e.getMessage(), e);
        }

        metricsLogger.putProperty("AccountId", "123456789");
        metricsLogger.putProperty("RequestId", "422b1569-16f6-4a03-b8f0-fe3fd9b100f8");
        metricsLogger.putProperty("DeviceId", "61270781-c6ac-46f1-baf7-22c808af8162");
        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("sampleTime", 123456789);
        payLoad.put("temperature", 273.0);
        payLoad.put("pressure", 101.3);
        metricsLogger.putProperty("Payload", payLoad);
        metricsLogger.flush();

        logger.info("completed aggregation successfully.");

        return "200 OK";
    }
}
