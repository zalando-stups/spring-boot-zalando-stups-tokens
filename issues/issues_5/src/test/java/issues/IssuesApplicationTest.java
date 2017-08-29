package issues;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.stups.tokens.AccessTokens;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IssuesApplication.class })
public class IssuesApplicationTest {

	@Autowired
	private AccessTokens accessTokens;

	@Test
	public void startUp() {
		Assertions.assertThat(accessTokens).isNotNull();
		String token = accessTokens.get("not_existent_service_id");
		Assertions.assertThat(token).isNotNull();
		Assertions.assertThat(token).isNotEmpty();
	}

}
