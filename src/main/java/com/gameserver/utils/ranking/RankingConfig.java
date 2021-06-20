package com.gameserver.utils.ranking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan("com.gameserver.utils")
@PropertySource("classpath:application.properties")
public class RankingConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${datasource.driver}")
    private String driver;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public List<String> rankingTypes() {
        return Arrays.asList("beginner", "warrior", "magician", "archer", "thief", "pirate", "world", "fame");
    }

    @Bean
    public Map<String, String> rankingQueries() {
        return new HashMap<>() {{
           for (int jobIndex = 0; jobIndex <= 5; jobIndex++ ) {
               put (rankingTypes().get(jobIndex), generateJobQuery(jobIndex));
           }
           put(rankingTypes().get(6), SELECT_WORLD_RANKING_QUERY);
           put(rankingTypes().get(7), SELECT_FAME_RANKING_QUERY);
        }};
    }

    public static final String SELECT_DATA_QUERY =
            "select c.id, c.name , g.name as guild, c.job, c.level, c.experience, c.fame " +
            "from game_world.character c " +
            // get guild name
            "left join game_world.guild_member gm " +
            "on c.id = gm.character_id " +
            "left join game_world.guild g " +
            "on gm.guild_id = g.id " +
            // exclude gm accounts
            "left join global.account a " +
            "on c.account_id = a.id " +
            "where a.gm_level = 0";

    private String generateJobQuery(int jobIndex) {
        return "select c.id " +
                "from game_world.character c " +
                "left join global.account a " +
                "on c.account_id = a.id " +
                "where a.gm_level = 0 " +
                "and c.job >= " + jobIndex + "00 and c.job <= " + jobIndex + "99 " +
                "order by c.level desc, c.experience desc, c.level_time asc";
    }

    private static final String SELECT_WORLD_RANKING_QUERY =
            "select c.id " +
            "from game_world.character c " +
            "left join global.account a " +
            "on c.account_id = a.id " +
            "where a.gm_level = 0 " +
            "order by c.level desc, c.experience desc, c.level_time asc";

    private static final String SELECT_FAME_RANKING_QUERY =
            "select c.id " +
            "from game_world.character c " +
            "left join global.account a " +
            "on c.account_id = a.id " +
            "where a.gm_level = 0 " +
            "order by c.fame";
}
