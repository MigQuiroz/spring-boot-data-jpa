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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import java.net.URI;
import java.util.HashMap;
import javax.annotation.PostConstruct;


import java.io.IOException;

import javax.net.ssl.SSLException;

import io.grpc.stub.StreamObserver;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.Subscriber;
import io.kubemq.sdk.event.EventReceive;
import io.kubemq.sdk.subscription.SubscribeRequest;
import io.kubemq.sdk.subscription.SubscribeType;
import io.kubemq.sdk.tools.Converter;
import io.kubemq.sdk.subscription.EventsStoreType;


@Controller
public class ClienteController implements StreamObserver<EventReceive>{
	
	@Autowired
	private IClienteDao clienteDao;
	
        Model md =null;
        String typeAlarm="";
        String description="";
        
        private Subscriber subscriber;
         
        public ClienteController(io.kubemq.sdk.event.Subscriber subscriber) {
            System.out.println("App client");
            this.subscriber = subscriber;
        }
        
       
             
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

                    model.addAttribute("sn",responseSearchOntDto.getList().get(0).getSn());
                    
                    model.addAttribute("olt",responseSearchOntDto.getList().get(0).getOlt());
                    model.addAttribute("frame",responseSearchOntDto.getList().get(0).getFrame());
                    model.addAttribute("slot",responseSearchOntDto.getList().get(0).getSlot());
                    model.addAttribute("port",responseSearchOntDto.getList().get(0).getPort());
                    model.addAttribute("ontid",responseSearchOntDto.getList().get(0).getOntID());
                    model.addAttribute("etiqueta",responseSearchOntDto.getList().get(0).getEtiqueta());
                    model.addAttribute("ipolt",responseSearchOntDto.getList().get(0).getIpolt());
                    
                    model.addAttribute("typecut","");
                    model.addAttribute("desc", "");
                    md =model;
                        
                    return "form";
		}
                
                
            } catch (Exception e) {
                
                System.out.println("error "+e);
            }

//		clienteDao.save(cliente);
            return "redirect:listar";
	}
        
        @RequestMapping(value="/form2", method = RequestMethod.POST )
	public String guardar4( Cliente cliente, BindingResult result, Model model ) {
            
            if(md!=null){
                model.addAttribute("typecut", typeAlarm);
                model.addAttribute("desc", description);
                model.addAttribute("sn", md.getAttribute("sn"));            
                model.addAttribute("olt", md.getAttribute("olt"));           
                model.addAttribute("frame", md.getAttribute("frame"));         
                model.addAttribute("slot", md.getAttribute("slot"));          
                model.addAttribute("port", md.getAttribute("port"));          
                model.addAttribute("ontid", md.getAttribute("ontid"));         
                model.addAttribute("etiqueta", md.getAttribute("etiqueta"));      
                model.addAttribute("ipolt", md.getAttribute("ipolt"));         

                System.out.println("typeAlarm: "+typeAlarm);
                System.out.println("desc: "+description);
               
            }
               
            
            return "form";
        }
  

        private static String objectToJson(Object obj) {
            Gson gson = new Gson();
            return gson.toJson(obj);
        }
            
        

        
        @Override
        public void onNext(EventReceive eventReceive) {
            eventReceive.getChannel();
            try {
                
                System.out.println("tipo de alarma ");
                System.out.println("type cut: %s {} "+ Converter.FromByteArray(eventReceive.getBody()));
                
                Map<String, String> mapdata = new HashMap<>();
                mapdata = (Map<String, String>) Converter.FromByteArray(eventReceive.getBody());
                
                for(Map.Entry<String, String> entry : mapdata.entrySet()) {                
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if(key.equals("typeCut")){
                        typeAlarm = value;
                    }else if(key.equals("desc")){
                        description = value;
                    }
                }        
                
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error Subscribe "+ e);
            }
        }

        @Override
        public void onError(Throwable thrwbl) {
        }

        @Override
        public void onCompleted() {
        }

        @PostConstruct
        public void subRe() {
            System.out.println("  *********** Subscribe ***********    ");

            SubscribeRequest subscribeRequest1 = new SubscribeRequest();

            try {

                subscribeRequest1.setChannel("type-alarm");
                subscribeRequest1.setClientID("client-type-alarm");
                subscribeRequest1.setSubscribeType(SubscribeType.EventsStore);
                subscribeRequest1.setEventsStoreType(EventsStoreType.StartNewOnly);
                
                
            } catch (Exception e) {
                System.out.println("SubscribeRequest type-alarm");
                System.out.println(e);
            }
            
            try {
                System.out.println("subscriber.getServerAddress() "+subscriber.getServerAddress());
                
                subscriber.SubscribeToEvents(subscribeRequest1, this);
            } catch (ServerAddressNotSuppliedException | SSLException e) {
                 System.out.println("subscribeRequest1");
                  System.out.println(e);
            }
                         
        }
        
}
