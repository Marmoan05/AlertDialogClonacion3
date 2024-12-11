package com.example.alertdialogclonacion3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppTareas()
            }
        }
    }
}

enum class Prioridad { HIGH, MEDIUM, LOW }

data class Tareas(
    val id: Int,
    var titulo: String,
    var completado: Boolean = false,
    var prioridad: Prioridad = Prioridad.LOW
)

@Composable
fun AppTareas() {
    var tareas by remember { mutableStateOf(tareasList()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Lista de Tareas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            val (pendingTasks, completedTasks) = tareas.partition { !it.completado }

            Text("Tareas Pendientes", fontWeight = FontWeight.Bold, color = Color.White)
            pendingTasks.forEach { tarea ->
                TarjetaTarea(tareas = tarea, onTaskUpdate = { updatedTask ->
                    tareas = tareas.map { if (it.id == updatedTask.id) updatedTask else it }
                })
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray)

            Text("Tareas Completadas", fontWeight = FontWeight.Bold, color = Color.White)
            completedTasks.forEach { tarea ->
                TarjetaTarea(tareas = tarea, onTaskUpdate = { updatedTask ->
                    tareas = tareas.map { if (it.id == updatedTask.id) updatedTask else it }
                })
            }
        }
    }
}

@Composable
fun TarjetaTarea(tareas: Tareas, onTaskUpdate: (Tareas) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when (tareas.prioridad) {
                                Prioridad.HIGH -> Color.Red
                                Prioridad.MEDIUM -> Color.Yellow
                                Prioridad.LOW -> Color.Green
                            },
                            shape = CircleShape
                        )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Text(tareas.titulo, modifier = Modifier.weight(1f), color = Color.White)
            MenuTareas(tareas = tareas, onTaskUpdate = onTaskUpdate)
        }
    }
}

@Composable
fun MenuTareas(tareas: Tareas, onTaskUpdate: (Tareas) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var priorityMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Opciones de tarea", tint = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.DarkGray)
        ) {
            DropdownMenuItem(
                text = { Text(if (tareas.completado) "Marcar como pendiente" else "Marcar como completada", color = Color.White) },
                onClick = {
                    expanded = false
                    onTaskUpdate(tareas.copy(completado = !tareas.completado))
                }
            )
            DropdownMenuItem(
                text = { Text("Cambiar prioridad", color = Color.White) },
                onClick = {
                    expanded = false
                    priorityMenuExpanded = true
                }
            )
        }

        if (priorityMenuExpanded) {
            DropdownMenu(
                expanded = priorityMenuExpanded,
                onDismissRequest = { priorityMenuExpanded = false },
                modifier = Modifier.background(Color.DarkGray)
            ) {
                Prioridad.entries.forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(priority.name, color = Color.White) },
                        onClick = {
                            priorityMenuExpanded = false
                            onTaskUpdate(tareas.copy(prioridad = priority))
                        }
                    )
                }
            }
        }
    }
}

fun tareasList(): List<Tareas> = listOf(
    Tareas(id = 1, titulo = "Estudiar para el examen", prioridad = Prioridad.HIGH),
    Tareas(id = 2, titulo = "Llorar porque es el examen de pepe", prioridad = Prioridad.HIGH),
    Tareas(id = 3, titulo = "Cocinar", completado = true, prioridad = Prioridad.LOW),
    Tareas(id = 4, titulo = "Ir a la aceitunas", prioridad = Prioridad.LOW),
    Tareas(id = 5, titulo = "Pasear al perro", prioridad = Prioridad.MEDIUM),
    Tareas(id = 6, titulo = "Hacer ejercicio", prioridad = Prioridad.HIGH),
    Tareas(id = 7, titulo = "Revisar correos", prioridad = Prioridad.LOW),
    Tareas(id = 8, titulo = "Planificar la semana", prioridad = Prioridad.MEDIUM),
    Tareas(id = 9, titulo = "Comprar víveres", prioridad = Prioridad.HIGH),
    Tareas(id = 10, titulo = "Llamar al médico", prioridad = Prioridad.MEDIUM),
    Tareas(id = 11, titulo = "Jugar videojuegos", prioridad = Prioridad.LOW)
)
