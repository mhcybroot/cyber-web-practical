package root.cyb.mh.cyberweb.controller;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SSTIController {

    private final ExpressionParser parser = new SpelExpressionParser();

    @GetMapping("/vuln/ssti")
    public String sstiPage() {
        return "vuln/ssti";
    }

    @PostMapping("/vuln/ssti")
    public String handleGrievance(@RequestParam String grievance, Model model) {
        String processedGrievance;
        try {
            // VULNERABLE: Evaluating user input directly as a SpEL expression
            if (grievance.contains("${") && grievance.contains("}")) {
                String expressionStr = grievance.substring(grievance.indexOf("${") + 2, grievance.lastIndexOf("}"));
                Expression exp = parser.parseExpression(expressionStr);
                Object value = exp.getValue();
                processedGrievance = grievance.replace("${" + expressionStr + "}",
                        value != null ? value.toString() : "null");

                if (expressionStr.equals("7*7")) {
                    model.addAttribute("flag", "FLAG{SP3L_1NJ3CT10N_M4ST3R}");
                }
            } else {
                processedGrievance = grievance;
            }
        } catch (Exception e) {
            processedGrievance = "Error processing grievance: " + e.getMessage();
        }

        model.addAttribute("result", processedGrievance);
        return "vuln/ssti";
    }
}
