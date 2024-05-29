package dev.alexcoss.universitycms.controller.generate;

import dev.alexcoss.universitycms.dto.data.GenerateDataRequest;
import dev.alexcoss.universitycms.service.UniversityDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GenerateDataController {

    private final UniversityDataService universityDataService;

    @GetMapping
    public String showGenerateDataForm(Model model) {
        model.addAttribute("generateDataRequest", new GenerateDataRequest());

        return "generated_data/generated_data";
    }

    @PostMapping("/dynamic")
    public String getDynamicData(@ModelAttribute("generateDataRequest") GenerateDataRequest generateDataRequest, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("generateDataRequest", generateDataRequest);
            return "generated_data/generated_data";
        }
        universityDataService.saveUniversityData(generateDataRequest);

        return "redirect:/";
    }

    @PostMapping("/static")
    public String getStaticData() {
        universityDataService.saveUniversityData();

        return "redirect:/";
    }
}
