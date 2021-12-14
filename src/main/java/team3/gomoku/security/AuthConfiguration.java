package team3.gomoku.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * 認証処理に関する設定（誰がログインできるか）
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    // pass:123
    auth.inMemoryAuthentication().withUser("player1")
        .password("$2y$10$udVaDVMUguMKa0genkRzU.zQWlOiayLdw6/DCevIn74GP5eQ4aVuC").roles("USER");

    auth.inMemoryAuthentication().withUser("player2")
        .password("$2y$10$lNpRyvOLBEzWZ.T52CsJEu4yA4tWwMsKWDSLRW5Dihjm.kX02VeVC").roles("USER");

    auth.inMemoryAuthentication().withUser("player3").password("$2y$10$3WfSImyNokYaY57gkwG.8OekXACrmP4G80ucCW4E2/aY1iZ1SMlU6").roles("USER");

    // $ sshrun htpasswd -nbBC 10 admin adm1n
    // htpasswdでBCryptエンコードを行った後の文字列をパスワードとして指定している．
    auth.inMemoryAuthentication().withUser("admin")
        .password("$2y$10$3e7Hs2QZ/p91yJVgP5y/1OC7AC8jfc6YDYDzMGK1XieDlNR2nBGDe").roles("ADMIN");
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Spring Securityのフォームを利用してログインを行う
    http.formLogin();
    // http://localhost:8000/sample3 で始まるURLへのアクセスはログインが必要
    // antMatchers().authenticated がantMatchersへのアクセスに認証を行うことを示す
    // antMatchers()の他にanyRequest()と書くとあらゆるアクセス先を表現できる
    // authenticated()の代わりにpermitAll()と書くと認証処理が不要であることを示す
    http.authorizeRequests().antMatchers("/game/**").authenticated();
    http.authorizeRequests().antMatchers("/gomoku1/**").authenticated();
    http.authorizeRequests().antMatchers("/gomoku2/**").authenticated();

    // Spring Securityの機能を利用してログアウト．ログアウト時は http://localhost:8000/ に戻る
    http.logout().logoutSuccessUrl("/");
     http.sessionManagement()
            // 同時ログイン数
            .maximumSessions(1)
            // ログインは先勝ち（true→先勝ち）
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry());

    /**
     * 以下2行はh2-consoleを利用するための設定なので，開発が完了したらコメントアウトすることが望ましい
     * CSRFがONになっているとフォームが対応していないためアクセスできない
     * HTTPヘッダのX-Frame-OptionsがDENYになるとiframeでlocalhostでのアプリが使えなくなるので，H2DBのWebクライアントのためだけにdisableにする必要がある
     */
    http.csrf().disable();
    http.headers().frameOptions().disable();
  }
   @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
            return new HttpSessionEventPublisher();
        }
  @Bean
  public SessionRegistry sessionRegistry() {
      SessionRegistry sessionRegistry = new SessionRegistryImpl();
      return sessionRegistry;
}
}
