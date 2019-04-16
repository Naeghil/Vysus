package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import storage.*;
import util.Conv;
import util.Ranking;

@Named("rankingGet")
@ConversationScoped
public class RankingGet extends VysusBean implements Serializable {
	
	public void createRanking() {
		try (Connection connection = getConnection()) {
			Ranking ranking = new Ranking();
			System.out.println(ranking.jobFilter("Computer Science", (float)400.0, connection));
			

		} catch (DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	
	
	

}