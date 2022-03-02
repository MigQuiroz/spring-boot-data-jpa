package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.RequestSearchOntDto;
import com.bolsadeideas.springboot.app.models.entity.ResponseSearchOntDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClienteController {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@RequestMapping(value = "listar",method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("Clientes", clienteDao.findAll());
		return "listar";
	}
	
	@RequestMapping(value="/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";		
	}
	
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		
		Cliente cliente =null;
		
		if(id > 0) {
			cliente = clienteDao.findOne(id);
		} else {
			return "redirect:/listar";
		}
		model.put("Cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
	@RequestMapping(value="/form", method = RequestMethod.POST )
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model ) {
            
 
            try {

                RestTemplate restTemplate = new RestTemplate();

                final String baseUrl = "http://localhost:8083/ont/GetSearchOntTest";
                URI uri = new URI(baseUrl);

                RequestSearchOntDto RS = new RequestSearchOntDto();
                
                System.out.println(" Serial Number "+cliente.getNombre());
                RS.setSerialNumber(cliente.getNombre());
               
                ResponseEntity<String> res = restTemplate.postForEntity(uri, RS, String.class);
                ResponseSearchOntDto responseSearchOntDto = new Gson().fromJson(res.getBody(), ResponseSearchOntDto.class);

                System.out.println("response 2: "+objectToJson(responseSearchOntDto) );
                 
                if (result.hasErrors()) {
                    System.out.println("valor "+responseSearchOntDto.getList().get(0).getEtiqueta());
                    model.addAttribute("sn",responseSearchOntDto.getList().get(0).getSn());
                    model.addAttribute("olt",responseSearchOntDto.getList().get(0).getOlt());
                    model.addAttribute("frame",responseSearchOntDto.getList().get(0).getFrame());
                    model.addAttribute("slot",responseSearchOntDto.getList().get(0).getSlot());
                    model.addAttribute("port",responseSearchOntDto.getList().get(0).getPort());
                    model.addAttribute("ontid",responseSearchOntDto.getList().get(0).getOntID());
                    model.addAttribute("etiqueta",responseSearchOntDto.getList().get(0).getEtiqueta());
                    model.addAttribute("ipolt",responseSearchOntDto.getList().get(0).getIpolt());
                    model.addAttribute("typecut",responseSearchOntDto.getList().get(0).getTypeCut());
                    model.addAttribute("desc", "Sin informacion de corte de fibra");
                    return "form";
		}
                
            } catch (Exception e) {
                
                System.out.println("error "+e);
            }

//		clienteDao.save(cliente);
            return "redirect:listar";
	}

        private static String objectToJson(Object obj) {
            Gson gson = new Gson();
            return gson.toJson(obj);
        }
            
}
