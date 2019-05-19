package managedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoURL;
import model.URL;
import validator.URLValidator;

@ManagedBean(name = "urlManagedBean")
@ViewScoped
public class URLManagedBean {
	private DaoURL<URL> daoURL = new DaoURL<URL>();
	private URL url = new URL();
	private URL urlRetorno = new URL();
	private List<URL> list = new ArrayList<URL>();
	
	public URL getUrl() {
		list = daoURL.listar(URL.class);
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public List<URL> getList() {
		return list;
	}

	public void salvar() {
		URLValidator validator = new URLValidator();
		if (validator.urlValidator(url.getUrlLonga())) {
			if (! url.getUrlLonga().equals("") && url.getUrlCurta() != null) {
				daoURL.salvar(url);		
				list = daoURL.listar(URL.class);
				url = new URL();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Salvo com sucesso!"));
			} else {
				if (url.getUrlLonga().equals("")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Informe a URL longa!"));
				}
				
				if (url.getUrlCurta() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "URL deve ser encurtada!"));
				}
			}
		} else {
			if (! validator.urlValidator(url.getUrlLonga())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "URL Longa inválida!"));
			}
		}
	}

	public void excluir() {
		try {
			daoURL.excluir(url);
			list.remove(url);
			url = new URL();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Excluído com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void encurtarURL() {
		URLValidator validator = new URLValidator();
		if (validator.urlValidator(url.getUrlLonga())) {
			urlRetorno = daoURL.buscar(url.getUrlLonga(), URL.class);
			
			if (urlRetorno == null) {
				url.setUrlCurta(gerarURLCurta());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "URL curta criada com sucesso!"));
			} else {
				url.setId(urlRetorno.getId());
				url.setUrlCurta(urlRetorno.getUrlCurta());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "URL curta já criada anteriormente!"));
			}		
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "URL Longa inválida!"));
		}
	}

    private String gerarURLCurta() {
        Random rand = new Random();
        int urlLen = 6;
        char[] shortURL = new char[urlLen];
        String randChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

        for (int i = 0; i < urlLen; i++ ) {
            shortURL[i] = randChars.charAt(rand.nextInt(randChars.length()));
        }

        StringBuilder sb = new StringBuilder("http://encurtadorURL.com/");
        sb.append(new String(shortURL));

        return sb.toString();
    }
}