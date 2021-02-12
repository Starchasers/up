package pl.starchasers.up.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminIndexController {
    @GetMapping("/admin", "/admin/**/{regex:\\w+}")
    fun adminPanelPath(): String {
        return "forward:/admin/index.html"
    }
}
