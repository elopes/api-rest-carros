/**
 * 
 */
package org.com.enlopes.api;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Elias
 *
 */
@RestController
@RequestMapping("/")
public class IndexController {

	@GetMapping
	public String get() {
		return "API Carros";
	}
	
	/*
	@GetMapping("/userInfo")
    public UserDetails userInfo(@AuthenticationPrincipal UserDetails user) {
        return user;
    }

	/*
	 * @PostMapping("/login") public String login(@RequestParam("login") String
	 * login, @RequestParam("senha") String senha) { return "Login " + login +
	 * " Senha " + senha; }
	 * 
	 * @PostMapping("/imovel/{id}") public String getImovelById(@PathVariable("id")
	 * Long id) { return "Imovel Id " + id; }
	 */
}
