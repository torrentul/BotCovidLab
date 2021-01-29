package lv.team3.botcovidlab.entityManager;

import com.google.firebase.internal.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class FirebaseController {

    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/getPatientDetails")
    public Patient getPatient(@RequestParam String personalCode ) throws InterruptedException, ExecutionException{
        return firebaseService.getPatientDetails(personalCode);
    }

    @PostMapping("/createPatient")
    public String createPatient(@RequestBody Patient patient ) throws InterruptedException, ExecutionException {
        return firebaseService.savePatientDetails(patient);
    }

    @PutMapping("/updatePatient")
    public String updatePatient(@RequestBody Patient patient  ) throws InterruptedException, ExecutionException {
        return firebaseService.updatePatientDetails(patient);
    }

    @DeleteMapping("/deletePatient")
    public String deletePatient(@RequestParam String personalCode){
        return firebaseService.deletePatient(personalCode);
    }


}
