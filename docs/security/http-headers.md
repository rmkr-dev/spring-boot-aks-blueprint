# HTTP security headers

## What ships

`SecurityHeadersFilter` (`dev.rmkr.blueprint.web`) adds conservative headers on every HTTP response:

| Header | Value | Intent |
| --- | --- | --- |
| `X-Content-Type-Options` | `nosniff` | Reduce MIME sniffing |
| `X-Frame-Options` | `DENY` | Block clickjacking via frames |
| `Referrer-Policy` | `no-referrer` | Avoid leaking URLs in Referer |
| `X-XSS-Protection` | `0` | Disable legacy XSS auditor (modern guidance) |
| `Permissions-Policy` | geolocation/mic/camera disabled | Tighten powerful browser features |

`SecurityHeadersFilter` is ordered after `RequestIdFilter` (`Ordered.HIGHEST_PRECEDENCE + 20` vs `+ 10`) so correlation ids are assigned first.

## What this is not

- Not a Content-Security-Policy for a SPA (this service returns JSON/Actuator text).
- Not a replacement for Ingress/WAF TLS or auth.
- Not Spring Security; no authentication filter chain is present.

## Verify locally

```bash
curl -sI http://localhost:8080/api/v1/hello | grep -iE 'x-content-type|x-frame|referrer-policy|permissions-policy|x-xss'
```
