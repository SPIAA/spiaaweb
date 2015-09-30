/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiaa.controller.api;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spiaa.model.ServiceLocator;
import spiaa.model.entity.TratamentoAntiVetorial;

/**
 *
 * @author William
 */
@Controller
public class TratamentoAntiVetorialApiController {
 
   private static final String RESPONSE_SUCCESS = "{\"success\":true}";
   private static final String RESPONSE_ERROR = "{\"success\":false}";

   @RequestMapping(value = "/boletim", method = RequestMethod.POST)
   public @ResponseBody
   String create(@RequestBody List<TratamentoAntiVetorial> tavs) {
      String resposta;
      try {
         for (TratamentoAntiVetorial tav : tavs) {
            ServiceLocator.getbaseBoletimDiarioService().create(tav);
         }
         resposta = RESPONSE_SUCCESS;
      } catch (Exception e) {
         resposta = RESPONSE_ERROR;
      }
      return resposta;
   }

}
