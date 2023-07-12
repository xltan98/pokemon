package sg.edu.nus.pafworkshop8.controller;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.nus.pafworkshop8.repositories.PokemonRepository;

@Controller
public class PokemonController {
    @Autowired
    PokemonRepository pRepo;

    @GetMapping("/")
    public String getPokemonTypeList(Model m){
        List<String> pokemonList= pRepo.getTypeofPokemon();

        m.addAttribute("pokemonList", pokemonList);

        return "index";

    }
    @GetMapping("/pokemon/{type}")
    public String getPokemonType(@PathVariable String type,Model m){
        List<Document> pokemonList=pRepo.getPokemonwithType(type);
        List<String> pokemonName=pokemonList.stream().map(d->d.getString("name")).toList();
         List<String> pokemonImg=pokemonList.stream().map(d->d.getString("img")).toList();
        m.addAttribute("pokemonName", pokemonName);
        m.addAttribute("pokemonImg", pokemonImg);

        // System.out.println(pokemonName);


        return "pokemontype";

    }
    
}
