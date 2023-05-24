package com.b2.notification.model;

import lombok.Generated;

@Generated
public enum StatusPembayaranNotif {
    MENUNGGU_PEMBAYARAN(1),
    MENUNGGU_KONFIRMASI(2),
    TERKONFIRMASI(3),
    BATAL(4);

    private Integer id;
    StatusPembayaranNotif(Integer id){
        this.id = id;
    }

    public String getMessage(){
        String status = this.name().toLowerCase().replace('_', ' ');
        return "Status terbaru Anda adalah " + status;
    }

    public Integer getId() {
        return id;
    }
}
