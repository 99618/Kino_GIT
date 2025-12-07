package com.example.projekt_filmy.config;

        import com.example.projekt_filmy.model.Movie;
        import com.example.projekt_filmy.model.User;
        import com.example.projekt_filmy.repository.MovieRepository;
        import com.example.projekt_filmy.repository.UserRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.boot.context.event.ApplicationReadyEvent;
        import org.springframework.context.event.EventListener;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        if(movieRepo.count()==0){
            movieRepo.save(Movie.builder().title("Oblivion").year(2012).director("Joseph Kosinski").build());
            movieRepo.save(Movie.builder().title("Ender's Game").year(2013).director("Gavin Hood").build());
            movieRepo.save(Movie.builder().title("Star Wars: Episode VII - The Force Awakens").year(2015).director("J.J. Abrams").build());
            movieRepo.save(Movie.builder().title("Star Wars: Episode VIII - The Last Jedi").year(2017).director("Rian Johnson").build());
            movieRepo.save(Movie.builder().title("Star Wars: Episode IX - The Rise of Skywalker").year(2019).director("J.J. Abrams").build());
            movieRepo.save(Movie.builder().title("Oppenheimer").year(2023).director("Christopher Nolan").build());
            movieRepo.save(Movie.builder().title("Avengers: Endgame").year(2019).director("Anthony Russo, Joe Russo").build());
            movieRepo.save(Movie.builder().title("Avengers: Infinity War").year(2018).director("Anthony Russo, Joe Russo").build());
            movieRepo.save(Movie.builder().title("Thor: Ragnarok").year(2017).director("Taika Waititi").build());
            movieRepo.save(Movie.builder().title("The Batman").year(2022).director("Matt Reeves").build());
        }
        if(userRepo.count()==0){
            User u = User.builder().firstName("Admin").lastName("Root").email("admin@gmail.com").password(encoder.encode("admin")).role(User.Role.ADMIN).build();
            userRepo.save(u);
        }
    }
}