package org.zalando.stups.tokens;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.unknown.pkg.TokenTestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes={TokenTestApplication.class})
@IntegrationTest
@ActiveProfiles("mocked")
public class MockAccessTokensBeanTest {
	
	@Autowired
	private AccessTokens accessTokens;

	@Test
	public void testMockAccessTokensBean(){
		String value = accessTokens.get("anyId");
		Assertions.assertThat(value).isEqualTo("INVALID");
	}
}
