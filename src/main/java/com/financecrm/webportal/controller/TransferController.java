package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.transfer.CreateTransferInput;
import com.financecrm.webportal.input.transfer.DeleteTransferInput;
import com.financecrm.webportal.input.transfer.GetAllTransfersByUserIdInput;
import com.financecrm.webportal.input.transfer.GetTransferByIdInput;
import com.financecrm.webportal.payload.transfer.CreateTransferPayload;
import com.financecrm.webportal.payload.transfer.DeleteTransferPayload;
import com.financecrm.webportal.payload.transfer.GetTransferPayload;
import com.financecrm.webportal.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/getTransferById")
    public ResponseEntity<GetTransferPayload> getTransferById(@RequestBody GetTransferByIdInput getTransferByIdInput){

        GetTransferPayload result = transferService.getTransferById(getTransferByIdInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }

    @GetMapping("/getAllTransfersByUserId")
    public Page<GetTransferPayload> getAllTransfersByUserId(@RequestBody GetAllTransfersByUserIdInput getAllTransfersByUserIdInput){
        return transferService.getAllTransfersByUserId(getAllTransfersByUserIdInput);
    }

    @PostMapping("/createTransfer")
    public ResponseEntity<CreateTransferPayload> createTransfer(@RequestBody CreateTransferInput createTransferInput) throws InterruptedException {
        CreateTransferPayload result = transferService.createTransfer(createTransferInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }

    @PostMapping("/deleteTransfer")
    public ResponseEntity<DeleteTransferPayload> deleteTransfer(@RequestBody DeleteTransferInput deleteTransferInput){
        DeleteTransferPayload result = transferService.deleteTransfer(deleteTransferInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }


}
