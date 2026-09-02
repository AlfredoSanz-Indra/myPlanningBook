package com.alfred.myplanningbook.data.repository.vehicle

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle
import com.alfred.myplanningbook.domain.repositoryapi.vehicle.VehicleRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2026
 */
class VehicleRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): VehicleRepository {

    override suspend fun insertVehicle(entity: Vehicle, userEmail: String): SimpleDataVehicleResponse = withContext(ioDispatcher) {
        Klog.line("VehicleRepositoryImpl", "insertVehicle", "name: ${entity.name}")

        val vehicleData = hashMapOf(
            Documents.VEHICLE_USEREMAIL to userEmail,
            Documents.VEHICLE_NAME to entity.name,
            Documents.VEHICLE_MODEL to entity.model,
            Documents.VEHICLE_NOTES to entity.notes,
            Documents.VEHICLE_ACQUISITION_DATE to entity.dateInMillis,
            Documents.VEHICLE_ACQUISITION_YEAR to entity.year,
            Documents.VEHICLE_ACQUISITION_MONTH to entity.month,
            Documents.VEHICLE_ACQUISITION_DAY to entity.day,
            Documents.VEHICLE_SALE_DATE to entity.saleDateInMillis,
            Documents.VEHICLE_SALE_YEAR to entity.saleYear,
            Documents.VEHICLE_SALE_MONTH to entity.saleMonth,
            Documents.VEHICLE_SALE_DAY to entity.saleDay
        )

        try {
            val docRef = FirebaseSession.db.collection(Collections.VEHICLES)
                .add(vehicleData)
                .await()

            Klog.line("VehicleRepositoryImpl", "insertVehicle", "Successfully inserted -> ID: ${docRef.id}")

            entity.id = docRef.id
            SimpleDataVehicleResponse(true, 200, "inserted vehicle").apply {
                this.vehicle = entity
            }

        } catch (e: Exception) {
            Klog.line("VehicleRepositoryImpl", "insertVehicle", "Error: ${e.message}")
            SimpleDataVehicleResponse(false, 400, "Inserting Vehicle failed: ${e.message}")
        }
    }

    override suspend fun getVehicles(userEmail: String): SimpleDataVehicleResponse = withContext(ioDispatcher) {
        Klog.line("VehicleRepositoryImpl", "getVehicles", "userEmail: $userEmail")

        try {
            val snapshot = FirebaseSession.db.collection(Collections.VEHICLES)
                .whereEqualTo(Documents.VEHICLE_USEREMAIL, userEmail)
                .orderBy(Documents.VEHICLE_ACQUISITION_DATE, Direction.DESCENDING)
                .get()
                .await()

            Klog.line("VehicleRepositoryImpl", "getVehicles", "snapshot -> $snapshot")

            val vehicleList = snapshot.documents.map { document ->
                Vehicle(
                    id = document.id,
                    name = document.get(Documents.VEHICLE_NAME) as String,
                    model = document.get(Documents.VEHICLE_MODEL) as String?,
                    notes = document.get(Documents.VEHICLE_NOTES) as String?,
                    dateInMillis = document.get(Documents.VEHICLE_ACQUISITION_DATE) as Long,
                    year = (document.get(Documents.VEHICLE_ACQUISITION_YEAR) as Long).toInt(),
                    month = (document.get(Documents.VEHICLE_ACQUISITION_MONTH) as Long).toInt(),
                    day = (document.get(Documents.VEHICLE_ACQUISITION_DAY) as Long).toInt(),
                    saleDateInMillis = document.get(Documents.VEHICLE_SALE_DATE) as Long?,
                    saleYear = (document.get(Documents.VEHICLE_SALE_YEAR) as Long?)?.toInt(),
                    saleMonth = (document.get(Documents.VEHICLE_SALE_MONTH) as Long?)?.toInt(),
                    saleDay = (document.get(Documents.VEHICLE_SALE_DAY) as Long?)?.toInt()
                )
            }
            Klog.line("VehicleRepositoryImpl", "getVehicles", "vehicleList -> $vehicleList")

            SimpleDataVehicleResponse(true, 200, "Vehicles obtained - ${vehicleList.size}").apply {
                this.vehicleList = vehicleList.toMutableList()
            }
        }
        catch (e: Exception) {
            Klog.line("VehicleRepositoryImpl", "getVehicles", "Error: ${e.message}")
            SimpleDataVehicleResponse(false, 400, "Getting Vehicles failed: ${e.message}")
        }
    }

    override suspend fun deleteVehicle(userEmail: String, vehicleId: String): SimpleDataVehicleResponse = withContext(ioDispatcher) {
        Klog.line("VehicleRepositoryImpl", "deleteVehicle", "vehicleId: $vehicleId")

        try {
            FirebaseSession.db.collection(Collections.VEHICLES)
                .document(vehicleId)
                .delete()
                .await()

            Klog.line("VehicleRepositoryImpl", "deleteVehicle", "Successfully deleted -> ID: $vehicleId")
            SimpleDataVehicleResponse(true, 200, "Vehicle deleted")
        }
        catch (e: Exception) {
            Klog.line("VehicleRepositoryImpl", "deleteVehicle", "Error: ${e.message}")
            SimpleDataVehicleResponse(false, 400, "Deleting Vehicle failed: ${e.message}")
        }
    }
}
