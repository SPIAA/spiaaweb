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
 
   @RequestMapping(value = "/boletim", method = RequestMethod.POST)
   public @ResponseBody
   void create(@RequestBody List<TratamentoAntiVetorial> tavs) throws Exception{
      try {
         for (TratamentoAntiVetorial tav : tavs) {
            ServiceLocator.getbaseBoletimDiarioService().create(tav);
         }
      } catch (Exception e) {
         throw e;
      }
   }

}
