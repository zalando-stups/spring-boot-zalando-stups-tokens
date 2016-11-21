package issues;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.zalando.stups.tokens.AccessTokens;

@Component
public class UsingComponent {

	private final Logger log = LoggerFactory.getLogger(UsingComponent.class);

	private final AccessTokens accessTokens;

	@Autowired
	public UsingComponent(AccessTokens accessTokens) {
		this.accessTokens = accessTokens;
	}

	@PostConstruct
	public void init() {
		String token = accessTokens.get("uid");
		Assert.hasText(token, "We expect a token here");
		log.info("will use : {} as Token", token);
	}
}
