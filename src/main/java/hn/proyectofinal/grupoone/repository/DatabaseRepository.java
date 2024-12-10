package hn.proyectofinal.grupoone.repository;


import hn.proyectofinal.grupoone.data.EmpleadosResponse;
import hn.proyectofinal.grupoone.data.Cursos;
import hn.proyectofinal.grupoone.data.CursosResponse;
import hn.proyectofinal.grupoone.data.Empleados;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DatabaseRepository {
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
	})
	@GET("/pls/apex/gestionuth/appgestion/empleados")//https://apex.oracle.com/
	Call<EmpleadosResponse> ObtenerEmpleados();
	
	@Headers({
		"Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
	})
	@POST("/pls/apex/gestionuth/appgestion/empleados")
	Call<ResponseBody> CrearEmpleados(@Body Empleados nuevo);
	
	@Headers({
		"Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
	})
	@PUT("/pls/apex/gestionuth/appgestion/empleados")
	Call<ResponseBody> ActualizarEmpleados(@Body Empleados nuevo);
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
		})
		@DELETE("/pls/apex/gestionuth/appgestion/cursos")
		Call<ResponseBody> eliminarEmpleado(@Query("id") Integer id);
	
	//CURSOS
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
	})
	  @GET("/pls/apex/gestionuth/appgestion/empleados")
	    Call<CursosResponse> obtenerCursos();
	
	    @Headers({
	        "Accept: application/json",
	        "Content-Type: application/json",
	        "User-Agent:Sistema gestion de desarrollo humano"
	    })
	    @POST("/pls/apex/gestionuth/appgestion/cursos")
	    Call<ResponseBody> agregarCurso(@Body Cursos nuevo);
	    
	    @Headers({
	        "Accept: application/json",
	        "Content-Type: application/json",
	        "User-Agent: Sistema gestion de desarrollo humano"
	    })
	    @PUT("/pls/apex/gestionuth/appgestion/cursos")
	    Call<ResponseBody> editarCurso(@Body Cursos curso);

		@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: Sistema gestion de desarrollo humano"
		})
		@DELETE("/pls/apex/gestionuth/appgestion/cursos")
		Call<ResponseBody> eliminarCurso(@Query("id") Integer id);
}