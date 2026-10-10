package com.unla.ghsicilianotfi.helpers;

public class ViewRouteHelper {
	/**** Views ****/
	//HOME
	public final static String INDEX = "home/index";
	public final static String HELLO = "home/hello";

	//DEGREE
	public final static String DEGREE_INDEX = "degree/index";
	public final static String DEGREE_FORM = "degree/form";
	public final static String DEGREE_NEW = "degree/new";

	//PERSON
	public final static String PERSON_INDEX = "person/index";
	public final static String PERSON_NEW = "person/new";
	public final static String PERSON_UPDATE = "person/update";
	public final static String PERSON_PARTIAL_VIEW = "person/partialPersonView";

	//PANELES POR ROL
	public final static String ADMIN_INDEX = "admin/index";
	public final static String RESPONSABLE_INDEX = "responsable/index";
	public final static String EMPLEADO_INDEX = "empleado/index";

	//REGISTRO
	public final static String REGISTRO_RESPONSABLE = "registro/responsable";

	//USER
	public final static String USER_LOGIN = "user/login";
	public final static String USER_LOGOUT = "user/logout";

	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String ADMIN_ROOT = "/admin";
	public final static String RESPONSABLE_ROOT = "/responsable";
	public final static String EMPLEADO_ROOT = "/empleado";
	public final static String DEGREE_ROOT = "/degrees/";
	public final static String PERSON_ROOT = "/person";
}
