
var pais = document.getElementById("paisId");
var provincia = document.getElementById("provinciaId");
var localidad = document.getElementById("localidadId");

// Guardamos las provincias originales
var provincias = [];

for (var i = 1; i < provincia.options.length; i++) {
    provincias.push(provincia.options[i]);
}

// Guardamos las localidades originales
var localidades = [];

for (var i = 1; i < localidad.options.length; i++) {
    localidades.push(localidad.options[i]);
}

// CUANDO CAMBIA EL PAIS
pais.onchange = function() {

    provincia.innerHTML = '<option value="">Selecciona una provincia</option>';
    localidad.innerHTML = '<option value="">Primero selecciona una provincia</option>';

    provincia.disabled = pais.value === "";
    localidad.disabled = true;

    for (var i = 0; i < provincias.length; i++) {

        if (provincias[i].getAttribute("data-pais") === pais.value) {
            provincia.appendChild(provincias[i]);
        }
    }
};

// CUANDO CAMBIA LA PROVINCIA
provincia.onchange = function() {

    localidad.innerHTML = '<option value="">Selecciona una localidad</option>';

    localidad.disabled = provincia.value === "";

    for (var i = 0; i < localidades.length; i++) {

        if (localidades[i].getAttribute("data-provincia") === provincia.value) {
            localidad.appendChild(localidades[i]);
        }
    }
};
