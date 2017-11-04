package logwrapper;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.workflow.entities.Execution;

public class LogWrapper {

	private static final String DB_NAME = "WorkflowExecution";
	private static final Logger LOG = LoggerFactory.getLogger(LogWrapper.class);
	private static Morphia morphia = new Morphia();
	private static MongoClient client = new MongoClient();

	static {
		morphia.mapPackageFromClass(Execution.class);
	}

	public static void saveExecution(Execution execution) {
		// Starter.LOG.info("Saving execution: {}", execution);
		Datastore datastore = morphia.createDatastore(client, DB_NAME);
		datastore.save(execution);

		// DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
		// Locale.ENGLISH);
		LOG.info("Execution saved");
	}

	public static void close() {
		if (client != null) {
			client.close();
		}
	}

	public static void printExecutions() {
		Datastore datastore = morphia.createDatastore(client, DB_NAME);

		Query<Execution> find = datastore.find(Execution.class);
		List<Execution> executions = find.asList();
		for (Execution execution : executions) {
			LOG.info("{}", execution);
		}

	}

}
